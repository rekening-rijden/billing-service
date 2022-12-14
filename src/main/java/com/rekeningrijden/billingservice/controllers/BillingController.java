package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.models.Vehicle;
import com.rekeningrijden.billingservice.services.BillingService;
import com.rekeningrijden.billingservice.services.DvlaService;
import com.rekeningrijden.billingservice.services.TaxConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/invoice/{carId}")
    public ResponseEntity<?> getAllInvoicesByCarId(@PathVariable String carId) {
        return this.billingService.getAllInvoices(carId);
    }

//    @PostMapping("/invoice")
//    public ResponseEntity<?> createInvoiceByRoutes(@RequestBody List<RouteDTO> routes) {
//        return this.billingService.createInvoiceByRoutes(routes);
//    }
//
//    @PostMapping("/invoice/{carId}")
//    public ResponseEntity<?> createInvoiceByCarId(@PathVariable String carId) {
//        return this.billingService.createInvoiceByCarId(carId);
//    }
    @PostMapping("/calculate")
    public ResponseEntity<?> calculatePrice(@RequestBody RouteDTO route) throws Exception {
        List<RoadTaxDto> roadTaxes = this.taxConfigService.getRoadTaxes();
        List<TimeTaxDto> timeTaxes = this.taxConfigService.getTimeTaxes();
        List<BasePriceDto> basePrices = this.taxConfigService.getBasePrices();
        String registrationNumber = "AA19AAA"; //this.garageService.getRegistrationNumberByCarId(routes.get(0).getCoords().get(0).getVehicleId());
        Vehicle vehicle = this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
        CalculatedPrice price = this.billingService.calculatePrice(route, basePrices, roadTaxes, timeTaxes, vehicle);
        return ResponseEntity.ok(price);
    }

    @PostMapping()
    public ResponseEntity<?> getAllInvoices() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("/getvehicle/{registrationNumber}")
    public ResponseEntity<?> getVehicleByRegistrationNumber(@PathVariable String registrationNumber) throws IOException, InterruptedException {
        Vehicle vehicle = this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(vehicle);
    }


}
