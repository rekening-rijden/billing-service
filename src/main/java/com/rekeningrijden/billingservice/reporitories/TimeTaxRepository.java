package com.rekeningrijden.billingservice.reporitories;

import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTaxRepository extends JpaRepository<TimeTaxDto, Integer> {


}
