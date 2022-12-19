package com.rekeningrijden.billingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.rekeningrijden.billingservice.DistanceCalculator.haversineDistance;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DistanceCalculatorTest {

    @Test
    public void testDistanceToPole() {
        // Test the distance from the equator to one of the poles
        double lat1 = 0;
        double lon1 = 0;
        double lat2 = 90;
        double lon2 = 0;
        double expected = 10007.5;
        double delta = 0.1;
        double actual = haversineDistance(lat1, lon1, lat2, lon2);
        assertEquals(expected, actual, delta);
    }

    @Test
    public void testHaversineDistance() {
        // Test the distance between two known coordinates
        double lat1 = 38.898556;
        double lon1 = -77.037852;
        double lat2 = 38.897147;
        double lon2 = -77.043934;
        double expected = 0.549;
        double delta = 0.001;
        double actual = haversineDistance(lat1, lon1, lat2, lon2);
        assertEquals(expected, actual, delta);
    }

    @Test
    public void testDistanceToSelf() {
        // Test the distance from a point to itself
        double lat = 38.898556;
        double lon = -77.037852;
        double expected = 0;
        double delta = 0.001;
        double actual = haversineDistance(lat, lon, lat, lon);
        assertEquals(expected, actual, delta);
    }
}
