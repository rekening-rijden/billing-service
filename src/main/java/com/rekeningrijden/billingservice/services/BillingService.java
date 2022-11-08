package com.rekeningrijden.billingservice.services;

import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;

    public BillingService(BillingRepository billlingRepository) {
        this.billlingRepository = billlingRepository;
    }

    public String test() {
        // return this.billingRepository.findAll();
        return "test";
    }
}
