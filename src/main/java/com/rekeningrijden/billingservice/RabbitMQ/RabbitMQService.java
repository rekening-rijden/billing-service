//package com.rekeningrijden.billingservice.RabbitMQ;
//
//
//import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
//import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
//import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
//import com.rekeningrijden.billingservice.services.TaxConfigService;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
//import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//
//@Component
//public class RabbitMQService implements RabbitListenerConfigurer {
//
//    @Autowired
//    TaxConfigService taxConfigService;
//
//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
//    }
//
////    @RabbitListener(queues = "${spring.rabbitmq.queue.baseprice}")
////    public List<String> updateBasePriceConfig(BasePriceDto basePriceDto) {
////        return taxConfigService.updateBasePriceConfig(basePriceDto);
////    }
////
////    @RabbitListener(queues = "${spring.rabbitmq.queue.baseprice}")
////    public List<String> updateRoadTaxConfig(RoadTaxDto roadTaxDto) {
////        return taxConfigService.updateRoadTaxConfig(roadTaxDto);
////    }
//
//    @RabbitListener(queues = "${spring.rabbitmq.queue.taxconfig}")
//    public List<String> updateTimeTaxConfig(TimeTaxDto timeTaxDto) {
//        return taxConfigService.updateTimeTaxConfig(timeTaxDto);
//    }
//
//    @RabbitListener(queues = "${spring.rabbitmq.queue.taxconfig}")
//    public List<String> updateRoadTaxConfig(TimeTaxDto timeTaxDto) {
//        return taxConfigService.updateTimeTaxConfig(timeTaxDto);
//    }
//}
