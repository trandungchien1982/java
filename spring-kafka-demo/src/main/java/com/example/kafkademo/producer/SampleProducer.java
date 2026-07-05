package com.example.kafkademo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SampleProducer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SampleProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final int messageCount;

    public SampleProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topic}") String topic,
            @Value("${app.kafka.message-count}") int messageCount
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.messageCount = messageCount;
    }

    @Override
    public void run(String... args) {
        for (int i = 1; i <= messageCount; i++) {
            String key = "key-" + (i % 3);
            String value = "Message " + i;

            kafkaTemplate.send(topic, key, value)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send message: key={}, value={}", key, value, ex);
                            return;
                        }

                        log.info(
                                "Produced: key={}, value={}, topic={}, partition={}, offset={}",
                                key,
                                value,
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    });
        }
    }
}
