package com.rekeningrijden.billingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CalculatedPrice {
    private BigDecimal totalDistance;
    private BigDecimal totalVehiclePrice;
    private BigDecimal totalRoadPrice;
    private BigDecimal totalTimePrice;
    private BigDecimal totalPrice;
    private String paymentLink;
}