package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.services.BillingService;
import com.rekeningrijden.billingservice.services.DvlaService;
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

    public BillingController(
            BillingService billingService,
            DvlaService dvlaService) {
        this.billingService = billingService;
        this.dvlaService = dvlaService;
    }

    @PostMapping("/createpayment")
    public ResponseEntity<?> createPaymentLink(@RequestBody PaymentInfoDTO paymentInfoDTO) {
        return this.billingService.createPaymentLink(paymentInfoDTO);
    }

    @GetMapping("/invoice/{carId}")
    public ResponseEntity<?> getAllInvoicesByCarId(@PathVariable String carId) {
        return this.billingService.getAllInvoices(carId);
    }

//    @PostMapping("/calculate")
//    public ResponseEntity<?> calculatePrice(@RequestBody PriceRequestDTO priceRequestDTO) {
//        return this.billingService.calculatePrice(priceRequestDTO);
//    }

    @PostMapping()
    public ResponseEntity<?> getAllInvoices() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("/getvehicle/{registrationNumber}")
    public ResponseEntity<?> getVehicleByRegistrationNumber(@PathVariable String registrationNumber) throws IOException, InterruptedException {
        return this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
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
}
