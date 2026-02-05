package org.example.cardservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountServiceClient {
    private final RestTemplate restTemplate;
    private final String accountServiceUrl = "http://account-service/accounts/";

    @Autowired
    public AccountServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean accountExists(Long accountId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(accountServiceUrl + accountId, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
