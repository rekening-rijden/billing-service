package com.rekeningrijden.billingservice.models.DTOs;

import com.rekeningrijden.billingservice.models.DataPoint;

import java.util.Date;
import java.util.List;

public class RouteDTO {
    private List<DataPoint> coords;
    private Date startTime;
    private Date endTime;
    private double distance;

    public RouteDTO(List<DataPoint> coords, Date startTime, Date endTime, double distance) {
        this.coords = coords;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
    }

    public List<DataPoint> getCoords() {
        return coords;
    }

    public void setCoords(List<DataPoint> coords) {
        this.coords = coords;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
