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
    private String carId;
    private Date date;
    private double amount;
    private String description;
    private BigDecimal pricePerKilometer;
}
