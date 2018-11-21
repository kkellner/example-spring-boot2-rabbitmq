package com.example.demo.queue;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServiceScan
public class RabbitPublisherConfig extends AbstractCloudConfig{

    private RabbitTemplate rabbitTemplate;

    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        System.out.println("RabbitTemplate init");
        rabbitTemplate =  new RabbitTemplate(connectionFactory().rabbitConnectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        //rabbitTemplate =  new RabbitTemplate(rabbitConnectionFactory);
        System.out.println("RabbitTemplate rabbitTemplate:"+rabbitTemplate);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}