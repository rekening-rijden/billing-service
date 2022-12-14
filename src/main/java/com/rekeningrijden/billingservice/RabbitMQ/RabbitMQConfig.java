package com.rekeningrijden.billingservice.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    //region TimeTax
    @Value("${spring.rabbitmq.queue.taxconfig}")
    private String queue;
    @Value("${spring.rabbitmq.routingkey.timetax}")
    private String routingKey;
    //endregion

    //region BasePrice
    @Value("${spring.rabbitmq.queue.baseprice}")
    private String basePriceQueue;
    @Value("${spring.rabbitmq.routingkey.baseprice}")
    private String basePriceRoutingKey;
    //endregion


    @Bean
    Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    Queue basePriceQueue() {
        return new Queue(basePriceQueue, true);
    }

    @Bean
    Exchange myExchange() {
        return ExchangeBuilder.topicExchange(exchange).durable(true).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(myExchange())
                .with(routingKey)
                .noargs();
    }

    @Bean
    Binding basePriceBinding() {
        return BindingBuilder
                .bind(basePriceQueue())
                .to(myExchange())
                .with(basePriceRoutingKey)
                .noargs();
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper().findAndRegisterModules());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}