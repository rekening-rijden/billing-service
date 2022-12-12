package com.rekeningrijden.billingservice.models.DTOs.TaxConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePriceDto {
    @Id
    private String engineType;
    private BigDecimal surTax;

}
