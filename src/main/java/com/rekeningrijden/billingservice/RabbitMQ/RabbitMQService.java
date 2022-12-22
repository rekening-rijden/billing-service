package com.rekeningrijden.billingservice.RabbitMQ;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.models.Invoice;
import com.rekeningrijden.billingservice.models.Vehicle;
import com.rekeningrijden.billingservice.services.BillingService;
import com.rekeningrijden.billingservice.services.DvlaService;
import com.rekeningrijden.billingservice.services.GarageService;
import com.rekeningrijden.billingservice.services.TaxConfigService;
import org.hibernate.mapping.Any;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;


@Component
public class RabbitMQService implements RabbitListenerConfigurer {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Autowired
    TaxConfigService taxConfigService;

    @Autowired
    BillingService billingService;
    @Autowired
    DvlaService dvlaService;
    @Autowired
    GarageService garageService;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.baseprice}")
    @SendTo("${spring.rabbitmq.queue.baseprice}")
    public BasePriceDto updateBasePriceConfig(String basePriceDto) throws JsonProcessingException {
        System.out.println("Received base price config: " + basePriceDto);
        BasePriceDto basePrice = objectMapper.readValue(basePriceDto, BasePriceDto.class);
        return taxConfigService.updateBasePriceConfig(basePrice);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.roadtax}")
    @SendTo("${spring.rabbitmq.queue.roadtax}")
    public List<String> updateRoadTaxConfig(String roadTaxDto) throws JsonProcessingException {
        System.out.println("Received road tax config: " + roadTaxDto);
        RoadTaxDto roadTax = objectMapper.readValue(roadTaxDto, RoadTaxDto.class);
        return taxConfigService.updateRoadTaxConfig(roadTax);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.timetax}")
    @SendTo("${spring.rabbitmq.queue.timetax}")
    public TimeTaxDto updateTimeTaxConfig(String timeTaxDto) throws JsonProcessingException {
        System.out.println("Received timetax config: " + timeTaxDto);
        TimeTaxDto timetax = objectMapper.readValue(timeTaxDto, TimeTaxDto.class);
        return taxConfigService.updateTimeTaxConfig(timetax);
    }

//    @RabbitListener(queues = "${spring.rabbitmq.queue.routes}")
//    @SendTo("${spring.rabbitmq.queue.routes}")
//    public void consumeRoute(String _route) throws Exception {
//        Gson gson = new Gson();
//        System.out.println("Received route: " + _route);
//        RouteDTO route = gson.fromJson(_route, RouteDTO.class);
//        List<RoadTaxDto> roadTaxes = this.taxConfigService.getRoadTaxes();
//        List<TimeTaxDto> timeTaxes = this.taxConfigService.getTimeTaxes();
//        List<BasePriceDto> basePrices = this.taxConfigService.getBasePrices();
//        int vehicleId = route.getCoords().get(0).getVehicleId();
//        String registrationNumber = garageService.getRegistrationNumber(vehicleId);
//        Vehicle vehicle = this.dvlaService.getVehicleByRegistrationNumber(registrationNumber);
//        CalculatedPrice price = billingService.calculatePrice(route, basePrices, roadTaxes, timeTaxes, vehicle);
//        this.billingService.createInvoice(vehicleId, price);
//    }
}
