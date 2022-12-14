package com.rekeningrijden.billingservice.services;

import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.models.DataPoint;
import com.rekeningrijden.billingservice.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillingServiceTest {
    @Autowired
    BillingService billingService;

    Vehicle vehicle;
    RouteDTO route;
    List<BasePriceDto> basePriceDtos;
    List<RoadTaxDto> roadTaxes;
    List<TimeTaxDto> timeTaxes;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle(
                "AA19AAA",
                "2025-03-30",
                300,
                2000,
                "EURO1",
                false,
                "PETROL",
                "No details held by DVLA",
                0,
                "RED",
                "FORD",
                "M1",
                2019,
                "2023-12-08",
                "Taxed",
                "2019-05-20",
                "2 AXLE RIGID BODY",
                "2019-03",
                "2019-03",
                "1"
        );

        List<DataPoint> dataPoints = Arrays.asList(
                new DataPoint(1, "route1", 1, new Date(), 51.454513, -2.587910, "HIGHWAY"),
                new DataPoint(2, "route1", 1, new Date(), 51.524569, -2.693598, "HIGHWAY")
        );
        route = new RouteDTO(
                dataPoints,
                new Date(),
                new Date(),
                0
        );

        basePriceDtos.add(new BasePriceDto("GASOLINE", new BigDecimal("0.10")));
        basePriceDtos.add(new BasePriceDto("DIESEL", new BigDecimal("0.30")));
        basePriceDtos.add(new BasePriceDto("ELECTRIC", new BigDecimal("0.00")));

        timeTaxes.add(new TimeTaxDto(1, new BigDecimal("0.10"), LocalTime.parse("00:00:00"), LocalTime.parse("00:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(2, new BigDecimal("0.10"), LocalTime.parse("01:00:00"), LocalTime.parse("01:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(3, new BigDecimal("0.10"), LocalTime.parse("02:00:00"), LocalTime.parse("02:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(4, new BigDecimal("0.10"), LocalTime.parse("03:00:00"), LocalTime.parse("03:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(5, new BigDecimal("0.10"), LocalTime.parse("04:00:00"), LocalTime.parse("04:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(6, new BigDecimal("0.10"), LocalTime.parse("05:00:00"), LocalTime.parse("05:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(7, new BigDecimal("0.10"), LocalTime.parse("06:00:00"), LocalTime.parse("06:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(8, new BigDecimal("0.10"), LocalTime.parse("07:00:00"), LocalTime.parse("07:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(9, new BigDecimal("0.10"), LocalTime.parse("08:00:00"), LocalTime.parse("08:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(10, new BigDecimal("0.10"), LocalTime.parse("09:00:00"), LocalTime.parse("09:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(11, new BigDecimal("0.10"), LocalTime.parse("10:00:00"), LocalTime.parse("10:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(12, new BigDecimal("0.10"), LocalTime.parse("11:00:00"), LocalTime.parse("11:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(13, new BigDecimal("0.10"), LocalTime.parse("12:00:00"), LocalTime.parse("12:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(14, new BigDecimal("0.10"), LocalTime.parse("13:00:00"), LocalTime.parse("13:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(15, new BigDecimal("0.10"), LocalTime.parse("14:00:00"), LocalTime.parse("14:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(16, new BigDecimal("0.10"), LocalTime.parse("15:00:00"), LocalTime.parse("15:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(17, new BigDecimal("0.10"), LocalTime.parse("16:00:00"), LocalTime.parse("16:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(18, new BigDecimal("0.10"), LocalTime.parse("17:00:00"), LocalTime.parse("17:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(19, new BigDecimal("0.10"), LocalTime.parse("18:00:00"), LocalTime.parse("18:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(20, new BigDecimal("0.10"), LocalTime.parse("19:00:00"), LocalTime.parse("19:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(21, new BigDecimal("0.10"), LocalTime.parse("20:00:00"), LocalTime.parse("20:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(22, new BigDecimal("0.10"), LocalTime.parse("21:00:00"), LocalTime.parse("21:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(23, new BigDecimal("0.10"), LocalTime.parse("22:00:00"), LocalTime.parse("22:59:59"), 0));
        timeTaxes.add(new TimeTaxDto(24, new BigDecimal("0.10"), LocalTime.parse("23:00:00"), LocalTime.parse("23:59:59"), 0));

        roadTaxes.add(new RoadTaxDto(1, new BigDecimal("0.10"), "HIGHWAY"));
        roadTaxes.add(new RoadTaxDto(2, new BigDecimal("0.10"), "CITY"));
        roadTaxes.add(new RoadTaxDto(3, new BigDecimal("0.10"), "MOTORWAY"));
    }
    @Test
    void calculatePrice() throws Exception {
        CalculatedPrice price = this.billingService.calculatePrice(route, basePriceDtos, roadTaxes, timeTaxes, vehicle);

    }
}