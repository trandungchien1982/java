package com.example.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic testTopic(
            @Value("${app.kafka.topic}") String topic,
            @Value("${app.kafka.partitions}") int partitions,
            @Value("${app.kafka.replicas}") int replicas
    ) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
