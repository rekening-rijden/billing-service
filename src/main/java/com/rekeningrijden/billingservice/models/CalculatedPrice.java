package com.rekeningrijden.billingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatedPrice {
    private double totalDistance;
    private double totalVehiclePrice;
    private double totalRoutePrice;
    private double totalTimePrice;
    private double totalPrice;
}
