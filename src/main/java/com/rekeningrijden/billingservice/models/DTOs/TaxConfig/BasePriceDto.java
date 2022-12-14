package com.rekeningrijden.billingservice.models.DTOs.TaxConfig;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class BasePriceDto {
    @Id
    private String engineType;
    private BigDecimal surTax;

    public BasePriceDto() {
    }
    public BasePriceDto(String engineType, BigDecimal surTax) {
        this.engineType = engineType;
        this.surTax = surTax;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public BigDecimal getSurTax() {
        return surTax;
    }

    public void setSurTax(BigDecimal surTax) {
        this.surTax = surTax;
    }
}
