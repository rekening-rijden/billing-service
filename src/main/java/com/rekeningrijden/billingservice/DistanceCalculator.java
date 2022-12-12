package com.rekeningrijden.billingservice;

import com.rekeningrijden.billingservice.models.DataPoint;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static java.awt.geom.Point2D.distance;

public class DistanceCalculator {

    private double distance = 0;
    private List<DataPoint> dataPointList;

    public DistanceCalculator(List<DataPoint> dataPointList) {
        this.dataPointList = dataPointList;
    }

    public DistanceCalculator() {
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    public double totalDistance()
    {
        int listSize = dataPointList.size();
        for (int i = 0; i < listSize-1; i++) {
            if (i != listSize-2)
            {
                distance += distance(dataPointList.get(i).getLat(), dataPointList.get(i+1).getLat(), dataPointList.get(i+1).getLng(), dataPointList.get(i+1).getLng());
            }
        }
        return (distance/1000);
    }

    // Calculate distance between two points in latitude and longitude taking into account height difference. If you are not interested in height difference pass 0.0. Uses Haversine method as its base.
    // This method returns the distance between two points in kilometers
    public static double haversineDistance(double _lat1, double _lon1, double _lat2, double _lon2) {
        // Convert the latitude and longitude values from degrees to radians
        double lat1 = Math.toRadians(_lat1);
        double lon1 = Math.toRadians(_lon1);
        double lat2 = Math.toRadians(_lat2);
        double lon2 = Math.toRadians(_lon2);

        // Calculate the differences between the coordinates
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        // Use the Haversine formula to calculate the distance
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Return the distance in kilometers
        return 6371 * c;
    }

}