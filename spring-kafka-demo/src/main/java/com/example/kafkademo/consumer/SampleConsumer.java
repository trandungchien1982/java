package com.example.kafkademo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SampleConsumer {

    private static final Logger log = LoggerFactory.getLogger(SampleConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        log.info(
                "Consumed: key={}, value={}, topic={}, partition={}, offset={}",
                record.key(),
                record.value(),
                record.topic(),
                record.partition(),
                record.offset()
        );
    }
}
