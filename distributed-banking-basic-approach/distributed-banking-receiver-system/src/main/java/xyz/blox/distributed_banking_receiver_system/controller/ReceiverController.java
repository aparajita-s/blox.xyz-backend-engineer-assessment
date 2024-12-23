package xyz.blox.distributed_banking_receiver_system.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receiver")
public class ReceiverController {

    @PostMapping("/credit")
    public String credit(@RequestParam String accountId, @RequestParam double amount) {
        // Mock credit operation
        System.out.println("Crediting " + amount + " to account " + accountId);
        return "Amount credited to " + accountId;
    }
}