package xyz.blox.distributed_banking_payee_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/payee")
public class PayeeController {

    @Value("${receiver.service.url}")
    private String receiverServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/transfer")
    public String transfer(@RequestParam String senderAccountId,
                           @RequestParam double amount,
                           @RequestParam String receiverAccountId) {
        // Mock debit operation
        System.out.println("Debiting " + amount + " from account " + senderAccountId);

        // Call the Receiver Service
        String creditUrl = receiverServiceUrl + "/receiver/credit";
        try {
            String response = restTemplate.postForObject(
                creditUrl + "?accountId=" + receiverAccountId + "&amount=" + amount,
                null, // No body
                String.class
            );
            return "Transfer Successful: " + response;
        } catch (Exception e) {
            return "Transfer Failed: " + e.getMessage();
        }
    }
}