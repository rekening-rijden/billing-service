package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.services.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/billing")
public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/createpayment")
    public ResponseEntity<?> createPaymentLink(@RequestBody PaymentInfoDTO paymentInfoDTO) {
        return this.billingService.createPaymentLink(paymentInfoDTO);
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
}
