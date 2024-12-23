package xyz.blox.enhanced_distributed_banking_receiver_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/receiver")
public class ReceiverController {

    // Set to store processed transaction IDs to ensure idempotency
    private Set<String> processedTransactionIds = new HashSet<>();

    @PostMapping("/credit")
    public ResponseEntity<String> creditAccount(@RequestParam String accountId,
                                                @RequestParam double amount,
                                                @RequestParam String senderId,
                                                @RequestHeader("Integrity-Hash") String integrityHash,
                                                @RequestHeader("Transaction-Id") String transactionId) {
        // Step 1: Check for duplicate transactions (idempotency)
        if (processedTransactionIds.contains(transactionId)) {
            return ResponseEntity.status(HttpStatus.OK).body("Duplicate request - Transaction already processed.");
        }

        // Step 2: Credit the receiver's account
        System.out.println("Crediting " + amount + " to account " + accountId);

        // Mark the transaction as processed to prevent future duplicates
        processedTransactionIds.add(transactionId);

        // Validate Integrity Hash
        String calculatedHash = generateHash(senderId + accountId + amount);
        System.out.println("Receiver's Integrity Hash: " + calculatedHash);

        if (!calculatedHash.equals(integrityHash)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Integrity check failed.");
        }

        return ResponseEntity.ok("Amount credited successfully.");
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