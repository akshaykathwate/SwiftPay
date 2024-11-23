package com.paye.whitelabel.controller;

import com.paye.whitelabel.model.Transaction;
import com.paye.whitelabel.service.EmailService;
import com.paye.whitelabel.service.PaymentService;
import com.paye.whitelabel.service.TransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService; // Add EmailService

    @Value("${STRIPE.SECRET.KEY}")
    private String apiKey;

    @PostMapping("/processPayment")
    @ResponseBody
    public Map<String, Object> processPayment(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();

        // Extract data from the request body
        String paymentToken = requestBody.get("paymentToken");
        String amountStr = requestBody.get("amount");
        String currency = requestBody.get("currency");
        String username = requestBody.get("loginusername"); // New field

        if (amountStr == null || currency == null) {
            response.put("status", "error");
            response.put("message", "Amount and Currency are required.");
            return response;
        }

        Integer amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            response.put("status", "error");
            response.put("message", "Invalid amount format.");
            return response;
        }

        // Prepare transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amountStr);
        transaction.setCurrency(currency);
        transaction.setDate(LocalDateTime.now());
        transaction.setUsername(username); // Set logged username

        // Process payment with Stripe
        boolean paymentSuccess = processPaymentWithToken(paymentToken, amount);

        // Update transaction status
        transaction.setSuccess(paymentSuccess);
        transaction.setStatus(paymentSuccess ? "Completed" : "Failed");
        transaction.setMessage(paymentSuccess ? "Payment successful" : "Payment failed");

        // Save transaction in the database
        Transaction savedTransaction = paymentService.processTransaction(transaction);

        // Prepare response
        response.put("status", paymentSuccess ? "success" : "failed");
        response.put("transactionId", savedTransaction.getTransactionId());
        response.put("message", savedTransaction.getMessage());

        // Send Email Notification
        emailService.sendTransactionEmail(savedTransaction);

        return response;
    }

    private boolean processPaymentWithToken(String token, Integer amount) {
        try {
            Stripe.apiKey = apiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount * 100); // Convert to cents
            params.put("currency", "usd");
            params.put("source", token);
            params.put("description", "Charge for payment processing");

            Charge charge = Charge.create(params);
            return charge.getPaid();
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
