package com.rekeningrijden.billingservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class DataPoint {
    @JsonIgnore
    private int id;
    private String routeId;
    private int vehicleId;
    private Date timestamp;
    private double lat;
    private double lng;
    private String roadType;
}
