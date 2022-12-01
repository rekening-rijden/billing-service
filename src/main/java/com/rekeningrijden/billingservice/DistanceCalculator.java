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
    public double calculateDistance(DataPoint dataPoint, DataPoint nextDataPoint) {
        double lat1 = dataPoint.getLat();
        double lon1 = dataPoint.getLng();
        double lat2 = nextDataPoint.getLat();
        double lon2 = nextDataPoint.getLng();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

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
        return distance;
    }
}
