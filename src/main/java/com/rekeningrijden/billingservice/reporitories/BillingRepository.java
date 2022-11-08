package com.rekeningrijden.billingservice.reporitories;

import com.rekeningrijden.billingservice.models.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Integer> {
}
