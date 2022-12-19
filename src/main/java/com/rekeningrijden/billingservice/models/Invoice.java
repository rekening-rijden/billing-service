package com.rekeningrijden.billingservice.models;

import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.RouteMatcher;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int carId;
    private Date date;
    private String description;
    private BigDecimal totalDistance;
    private BigDecimal totalVehiclePrice;
    private BigDecimal totalRoadPrice;
    private BigDecimal totalTimePrice;
    private BigDecimal totalPrice;
    private String paymentLink;
}
