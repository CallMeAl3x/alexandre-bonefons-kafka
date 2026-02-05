package org.example.loansservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loansservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanKafkaConsumer {

    @Autowired
    private LoanService loanService;

    @KafkaListener(topics = "account-events", groupId = "loan-group")
    public void consumeAccountDeletedEvent(String message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode event = objectMapper.readTree(message);

            if ("ACCOUNT_DELETED".equals(event.get("event").asText())) {
                Long accountId = Long.valueOf(event.get("accountId").asText());
                log.info("ACCOUNT_DELETED: accountId: {}", accountId);
                loanService.deleteLoanByAccountId(accountId);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
