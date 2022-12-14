package com.rekeningrijden.billingservice.reporitories;

import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePriceRepository extends JpaRepository<BasePriceDto, Integer> {
    boolean existsByEngineType(String engineType);
    BasePriceDto findByEngineType(String engineType);
}
