package org.example.accountservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.entity.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountKafkaConsumer {

    @Autowired
    private AccountRepository accountRepository;

    // Écoute les événements des Cards
    @KafkaListener(topics = "card-events", groupId = "account-group")
    public void consumeCardEvent(String message) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode event = mapper.readTree(message);

            String eventType = event.get("event").asText();
            Long accountId = Long.valueOf(event.get("accountId").asText());

            log.info("Received card event: {} for account: {}", eventType, accountId);

            // Récupérer l'Account
            Account account = accountRepository.findById(accountId).orElse(null);

            if (account != null) {
                if ("CARD_CREATED".equals(eventType)) {
                    account.setTotalCards(account.getTotalCards() + 1);
                } else if ("CARD_DELETED".equals(eventType)) {
                    account.setTotalCards(account.getTotalCards() - 1);
                }
                accountRepository.save(account);
                log.info("Updated totalCards for account {}: {}", accountId, account.getTotalCards());
            }

        } catch (JsonProcessingException e) {
            log.error("Error parsing card event", e);
        }
    }

    // Écoute les événements des Loans
    @KafkaListener(topics = "loan-events", groupId = "account-group")
    public void consumeLoanEvent(String message) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode event = mapper.readTree(message);

            String eventType = event.get("event").asText();
            Long accountId = Long.valueOf(event.get("accountId").asText());

            log.info("Received loan event: {} for account: {}", eventType, accountId);

            Account account = accountRepository.findById(accountId).orElse(null);

            if (account != null) {
                if ("LOAN_CREATED".equals(eventType)) {
                    account.setTotalLoans(account.getTotalLoans() + 1);
                } else if ("LOAN_DELETED".equals(eventType)) {
                    account.setTotalLoans(account.getTotalLoans() - 1);
                }
                accountRepository.save(account);
                log.info("Updated totalLoans for account {}: {}", accountId, account.getTotalLoans());
            }

        } catch (JsonProcessingException e) {
            log.error("Error parsing loan event", e);
        }
    }
}