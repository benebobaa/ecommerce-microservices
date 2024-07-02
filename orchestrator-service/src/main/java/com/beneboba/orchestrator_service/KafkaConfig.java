package com.beneboba.orchestrator_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderCreateRequest() {
        return TopicBuilder
                .name("order-create-request")
                .build();
    }

    @Bean
    public NewTopic productCheck() {
        return TopicBuilder
                .name("product-check")
                .build();
    }

    @Bean
    public NewTopic orderProductCreated() {
        return TopicBuilder
                .name("order-product-created")
                .build();
    }

    @Bean
    public NewTopic orderProductRevert(){
        return TopicBuilder
                .name("order-product-revert")
                .build();
    }

    @Bean
    public NewTopic paymentEvent(){
        return TopicBuilder
                .name("payment-events")
                .build();
    }

    @Bean
    public NewTopic paymentInitiate(){
        return TopicBuilder
                .name("payment-initiate")
                .build();
    }

    @Bean
    public NewTopic paymentSuccess(){
        return TopicBuilder
                .name("payment-success")
                .build();
    }

    @Bean
    public NewTopic paymentFailed(){
        return TopicBuilder
                .name("payment-failed")
                .build();
    }

    @Bean
    public NewTopic orderProductPaymentComplete(){
        return TopicBuilder
                .name("order-product-payment-complete")
                .build();
    }

    @Bean
    public NewTopic orderProductPaymentFailed(){
        return TopicBuilder
                .name("order-product-payment-failed")
                .build();
    }

}
