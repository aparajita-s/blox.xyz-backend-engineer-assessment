package xyz.blox.enhanced_distributed_banking_payee_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/payee")
public class PayeeController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${receiver.service.url}")
    private String receiverServiceUrl;

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String senderAccountId,
                                           @RequestParam double amount,
                                           @RequestParam String receiverAccountId) {
        // Step 1: Generate a unique transaction ID
        String transactionId = UUID.randomUUID().toString();
        System.out.println("tnxID: " + transactionId);

        // Step 2: Debit the sender's account
        boolean isDebited = debitAccount(senderAccountId, amount);

        if (!isDebited) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to debit sender account.");
        }

        // Step 3: Generate a hash for data integrity (optional)
        String payload = senderAccountId + receiverAccountId + amount;
        String integrityHash = generateHash(payload);
        System.out.println("Payee's Integrity Hash: " + integrityHash);

        // Step 4: Call the Receiver Service to credit the amount
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Integrity-Hash", integrityHash);
            headers.set("Transaction-Id", transactionId); // Include transaction ID for idempotency

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            if (receiverAccountId == senderAccountId) {
                throw new Exception("Amount can not be debited to same account.");
            }
            
            String url = receiverServiceUrl + "/receiver/credit?accountId=" + receiverAccountId + "&amount=" + amount + "&senderId=" + senderAccountId;
            System.out.println("URL: " + url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new Exception("Receiver service failed.");
            }

            return ResponseEntity.ok("Transfer Successful");
        } catch (Exception e) {
            // Rollback if something fails
            rollbackDebit(senderAccountId, amount);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transfer Failed: " + e.getMessage());
        }
    }

    private boolean debitAccount(String accountId, double amount) {
        // Mock logic to debit the account
        System.out.println("Debited " + amount + " from account " + accountId);
        return true;
    }

    private void rollbackDebit(String accountId, double amount) {
        // Mock logic to rollback debit
        System.out.println("Rolled back " + amount + " to account " + accountId);
    }

    private String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}