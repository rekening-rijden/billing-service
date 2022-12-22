package com.rekeningrijden.billingservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rekeningrijden.billingservice.models.Vehicle;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GarageService {
    private final String garageUrl = "http://localhost:5296/api/car/";
    private final ObjectMapper objectMapper;

    public GarageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }
    public String getRegistrationNumber(int vehicleId) throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(garageUrl + vehicleId))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(getResponse.body());
        Gson gson = new Gson();
        return gson.fromJson(getResponse.body(), String.class);
    }
}
