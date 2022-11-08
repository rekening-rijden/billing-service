package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.services.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/billing")
@CrossOrigin(origins = "*")
public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/test")
    public String test() {
        return this.billingService.test();
    }

    @PostMapping("/createpayment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentInfoDTO paymentInfoDTO) {
        return this.billingService.createPayment(paymentInfoDTO);
    }

    @GetMapping("/getpayment/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable String paymentId) {
        return this.billingService.getPaymentById(paymentId);
    }
}
