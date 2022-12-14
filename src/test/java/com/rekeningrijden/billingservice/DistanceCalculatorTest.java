package com.rekeningrijden.billingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DistanceCalculatorTest {

    @Test
    void haversineDistance() {
        double distance = DistanceCalculator.haversineDistance(51.454513, -2.587910, 51.524569, -2.693598);
        System.out.println(distance);
    }
}