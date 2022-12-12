package com.rekeningrijden.billingservice.services;

import com.rekeningrijden.billingservice.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillingServiceTest {
    @Autowired
    BillingService billingService;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        Vehicle vehicle = new Vehicle(
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
    }

    @Test
    void calculatePrice() {

    }
}