package com.beneboba.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.order-request.topic}")
    private String orderTopic;

    @Value("${kafka.order-validate.topic}")
    private String orderValidateTopic;

    @Value("${kafka.order-event.topic}")
    private String orderEventTopic;

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name(orderTopic)
                .build();
    }

    @Bean
    public NewTopic orderValidateTopic() {
        return TopicBuilder
                .name(orderValidateTopic)
                .build();
    }

    @Bean
    public NewTopic orderEventTopic() {
        return TopicBuilder
                .name(orderEventTopic)
                .build();
    }
}
