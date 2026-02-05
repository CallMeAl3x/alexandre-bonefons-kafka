package org.example.loansservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoanKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topic.loans:loan-events}")
    private String topic;

    public void sendLoanCreated(Long accountId) {
        String event = String.format(
                "{\"event\":\"LOAN_CREATED\",\"accountId\":\"%s\"}",
                accountId
        );
        log.info("Producing loan created event: {}", event);
        kafkaTemplate.send(topic, event);
    }

    public void sendLoanDeleted(Long accountId) {
        String event = String.format(
                "{\"event\":\"LOAN_DELETED\",\"accountId\":\"%s\"}",
                accountId
        );
        log.info("Producing loan deleted event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}
