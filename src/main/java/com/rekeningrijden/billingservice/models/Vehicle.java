package com.rekeningrijden.billingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String registrationNumber;
    private String artEndDate;
    private int co2Emissions;
    private int engineCapacity;
    private String euroStatus;
    private boolean markedForExport;
    private String fuelType;
    private String motStatus;
    private int revenueWeight;
    private String colour;
    private String make;
    private String typeApproval;
    private int yearOfManufacture;
    private String taxDueDate;
    private String taxStatus;
    private String dateOfLastV5CIssued;
    private String wheelplan;
    private String monthOfFirstDvlaRegistration;
    private String monthOfFirstRegistration;
    private String realDrivingEmissions;
}
