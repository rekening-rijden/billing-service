package com.rekeningrijden.billingservice.models;

import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import lombok.*;
import org.springframework.util.RouteMatcher;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String paymentLink;
    private String carId;
    private Date date;
    private double price;
    private String description;
    private BigDecimal pricePerKilometer;
}
