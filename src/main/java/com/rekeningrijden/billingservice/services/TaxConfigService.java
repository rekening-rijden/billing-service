package com.rekeningrijden.billingservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.reporitories.BasePriceRepository;
import com.rekeningrijden.billingservice.reporitories.RoadTaxRepository;
import com.rekeningrijden.billingservice.reporitories.TimeTaxRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class TaxConfigService {

    private final String roadTaxUrl = "http://localhost:8089/roadtax/all";
    private final String timeTaxUrl = "http://localhost:8089/timetax/all";
    private final String basePriceUrl = "http://localhost:8089/baseprice/all";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final BasePriceRepository basePriceRepository;
    private final RoadTaxRepository roadTaxRepository;
    private final TimeTaxRepository timeTaxRepository;

    public TaxConfigService(BasePriceRepository basePriceRepository, RoadTaxRepository roadTaxRepository, TimeTaxRepository timeTaxRepository) {
        this.basePriceRepository = basePriceRepository;
        this.roadTaxRepository = roadTaxRepository;
        this.timeTaxRepository = timeTaxRepository;
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    public List<RoadTaxDto> getRoadTaxes() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(roadTaxUrl))
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .build();

        String jsonResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        List<RoadTaxDto> roadTaxes = Arrays.stream(objectMapper.readValue(jsonResponse, RoadTaxDto[].class)).toList();
        return roadTaxes;
    }

    public List<TimeTaxDto> getTimeTaxes() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(timeTaxUrl))
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .build();

        String jsonResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        List<TimeTaxDto> timeTaxes = Arrays.stream(objectMapper.readValue(jsonResponse, TimeTaxDto[].class)).toList();
        return timeTaxes;
    }

    public List<BasePriceDto> getBasePrices() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(basePriceUrl))
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .build();

        String jsonResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        List<BasePriceDto> basePrices = Arrays.stream(objectMapper.readValue(jsonResponse, BasePriceDto[].class)).toList();
        return basePrices;
    }

    //region Messaging

    public List<String> updateBasePriceConfig(BasePriceDto basePriceDto) {
        // if baseprice already exists, update. else, create new
        return null;
    }

    public List<String> updateRoadTaxConfig(RoadTaxDto roadTaxDto) {
//        if(count(roadTaxDto.getRoadType()) == 0) {
//            // add to database
//        } else {
//            // update in database
//        }
        return null;
    }

    public List<String> updateTimeTaxConfig(TimeTaxDto timeTaxDto) {
        TimeTaxDto timeTaxToUpdate = this.timeTaxRepository.findById(timeTaxDto.getId()).orElseThrow();
        timeTaxToUpdate.setSurTax(timeTaxDto.getSurTax());
        this.timeTaxRepository.save(timeTaxToUpdate);
        // update in database
        System.out.println("I got a timetax! : " + timeTaxDto);
        return null;
    }

    //endregion
}
