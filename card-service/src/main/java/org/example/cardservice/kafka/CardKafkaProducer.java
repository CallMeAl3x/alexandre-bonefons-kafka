package org.example.cardservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CardKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topic.cards:card-events}")
    private String topic;

    public void sendCardCreated(Long accountId) {
        String event = String.format(
                "{\"event\":\"CARD_CREATED\",\"accountId\":\"%s\"}",
                accountId
        );
        log.info("Producing card created event: {}", event);
        kafkaTemplate.send(topic, event);
    }

    public void sendCardDeleted(Long accountId) {
        String event = String.format(
                "{\"event\":\"CARD_DELETED\",\"accountId\":\"%s\"}",
                accountId
        );
        log.info("Producing card deleted event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}
