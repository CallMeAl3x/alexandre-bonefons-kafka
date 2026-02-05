package org.example.accountservice.kafka;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountKafkaProducer {

    private  final KafkaTemplate<String, String> kafkaTemplate;

    public AccountKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Value("${spring.kafka.topic.account}")
    private String topic;

    public void sendAccountDelete(Long accountId) {
        String event = String.format(
                "{\"event\":\"ACCOUNT_DELETED\",\"accountId\":\"%s\"}",
                accountId
        );
        log.info("Producing account deleted event: {}", event);
        kafkaTemplate.send(topic, event);
    }

}
