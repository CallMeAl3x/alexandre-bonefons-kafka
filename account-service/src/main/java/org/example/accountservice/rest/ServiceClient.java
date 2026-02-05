package org.example.accountservice.rest;

import org.example.accountservice.dto.CardDTO;
import org.example.accountservice.dto.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
public class ServiceClient {
    private final RestTemplate restTemplate;
    private static final String CARD_SERVICE_URL = "http://card-service/cards/byAccount/";
    private static final String LOAN_SERVICE_URL = "http://loans-service/loans/byAccount/";

    @Autowired
    public ServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CardDTO> getCardsByAccountId(Long accountId) {
        try {
            ResponseEntity<List<CardDTO>> response = restTemplate.exchange(
                    CARD_SERVICE_URL + accountId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CardDTO>>() {});
            return response.getBody();
        } catch (Exception e) {
            // Log the error or handle it as necessary
            return Collections.emptyList();
        }
    }

    public List<LoanDTO> getLoansByAccountId(Long accountId) {
        try {
            ResponseEntity<List<LoanDTO>> response = restTemplate.exchange(
                    LOAN_SERVICE_URL + accountId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LoanDTO>>() {});
            return response.getBody();
        } catch (Exception e) {
            // Log the error or handle it as necessary
            return Collections.emptyList();
        }
    }
}
