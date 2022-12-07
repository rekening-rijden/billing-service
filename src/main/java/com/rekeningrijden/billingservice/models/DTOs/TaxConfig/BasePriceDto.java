package com.rekeningrijden.billingservice.models.DTOs.TaxConfig;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class BasePriceDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal kilometerTax;
    private String engineType;

    public BasePriceDto() {}

    public BasePriceDto(BigDecimal kilometerTax, String engineType) {
        this.kilometerTax = kilometerTax;
        this.engineType = engineType;
    }

    public BigDecimal getKilometerTax() {
        return kilometerTax;
    }

    public void setKilometerTax(BigDecimal kilometerTax) {
        this.kilometerTax = kilometerTax;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }
}
