package com.paye.whitelabel.controller;

import com.paye.whitelabel.model.Transaction;
import com.paye.whitelabel.model.User;
import com.paye.whitelabel.service.TransactionService;
import com.paye.whitelabel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Changed to @RestController for JSON responses
@RequestMapping("/api")  // Add an API prefix for clarity
@CrossOrigin(origins = "http://localhost:4200")
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {
        // Check if the username already exists
        if (userService.findUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        // Check if the email already exists
        if (userService.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
        }


        // Check if the email is provided
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required.");
        }

        // Register the user
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful! You can now log in.");
    }
}
