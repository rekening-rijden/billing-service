package com.rekeningrijden.billingservice.controllers;

import com.rekeningrijden.billingservice.services.BillingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
