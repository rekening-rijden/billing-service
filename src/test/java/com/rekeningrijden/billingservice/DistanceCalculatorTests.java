package com.rekeningrijden.billingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DistanceCalculatorTests {


    @Test
    public void testDistanceCalculator() {
        double distance = DistanceCalculator.calculateDistance(51.454513, -2.587910, 51.524569, -2.693598);
        System.out.println(distance);
    }

    @Test
    public void testDistanceCalculator2() {
        double distance = DistanceCalculator.haversineDistance(51.454513, -2.587910, 51.524569, -2.693598);
        System.out.println(distance);
    }
}
