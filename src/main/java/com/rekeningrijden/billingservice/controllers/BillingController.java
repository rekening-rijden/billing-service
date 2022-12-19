package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.models.Invoice;
import com.rekeningrijden.billingservice.models.Vehicle;
import com.rekeningrijden.billingservice.services.BillingService;
import com.rekeningrijden.billingservice.services.DvlaService;
import com.rekeningrijden.billingservice.services.TaxConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/billing")
public class BillingController {
    private final BillingService billingService;
    private final DvlaService dvlaService;
    private final TaxConfigService taxConfigService;

    public BillingController(
            BillingService billingService,
            DvlaService dvlaService,
            TaxConfigService taxConfigService) {
        this.billingService = billingService;
        this.dvlaService = dvlaService;
        this.taxConfigService = taxConfigService;
    }

    @GetMapping("/invoice/getall/{carId}")
    public ResponseEntity<?> getAllInvoicesByCarId(@PathVariable int carId) {
        return this.billingService.getAllInvoices(carId);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> getInvoiceById(@PathVariable int invoiceId) {
        return ResponseEntity.ok(this.billingService.getInvoiceById(invoiceId));
    }
    @PostMapping("/calculate")
    public ResponseEntity<?> calculatePrice(@RequestBody RouteDTO route) throws Exception {
        List<RoadTaxDto> roadTaxes = this.taxConfigService.getRoadTaxes();
        List<TimeTaxDto> timeTaxes = this.taxConfigService.getTimeTaxes();
        List<BasePriceDto> basePrices = this.taxConfigService.getBasePrices();
        int vehicleId = route.getCoords().get(0).getVehicleId();
        String registrationNumber = "AA19AAA"; //this.garageService.getRegistrationNumberByCarId(routes.get(0).getCoords().get(0).getVehicleId());
        Vehicle vehicle = this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
        CalculatedPrice price = this.billingService.calculatePrice(route, basePrices, roadTaxes, timeTaxes, vehicle);
        Invoice invoice = this.billingService.createInvoice(vehicleId, price);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/getvehicle/{registrationNumber}")
    public ResponseEntity<?> getVehicleByRegistrationNumber(@PathVariable String registrationNumber) throws IOException, InterruptedException {
        Vehicle vehicle = this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(vehicle);
    }


}
