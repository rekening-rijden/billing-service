package com.rekeningrijden.billingservice.reporitories;

import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadTaxRepository extends JpaRepository<RoadTaxDto, Integer> {

}
