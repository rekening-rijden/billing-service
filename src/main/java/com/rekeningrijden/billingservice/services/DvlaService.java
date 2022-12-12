package com.rekeningrijden.billingservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rekeningrijden.billingservice.models.Vehicle;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DvlaService {
    private final ObjectMapper objectMapper;
    public DvlaService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    // a method that sends a post request with a json body to the dvla test api
    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://uat.driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles"))
                .header("Content-Type", "application/json")
                .header("x-api-key", "qTglyIAeB94hirBiJdd6ua6E2R0IUfSe43PBo36A")
                .POST(HttpRequest.BodyPublishers.ofString("{\"registrationNumber\": \"" + registrationNumber + "\"}"))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
        return objectMapper.readValue(postResponse.body(), Vehicle.class);
    }
}