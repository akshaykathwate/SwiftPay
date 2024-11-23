package com.paye.whitelabel.controller;

import com.paye.whitelabel.model.Transaction;
import com.paye.whitelabel.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/by-username")
    public ResponseEntity<?> getTransactionsByUsername(@RequestParam String username) {
        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);

        if (transactions.isEmpty()) {
            // Provide meaningful message when no transactions are found
            Map<String, String> response = new HashMap<>();
            response.put("message", "No transactions found for the username: " + username);
            response.put("status", "204 No Content");
            return ResponseEntity.status(204).body(response); // Returning response with status and message
        }

        return ResponseEntity.ok(transactions); // Return transactions if found
    }
}
