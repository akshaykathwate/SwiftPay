package com.paye.whitelabel.controller;

import com.paye.whitelabel.model.User;
import com.paye.whitelabel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        // Find the user by username
        user = userService.findUserByUsername(username);

        // Check if the user exists and if the password is correct
        if (user != null && password.equals(user.getPassword())) {
            // Return the response in JSON format
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Login successful! Welcome " + username));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("Invalid username or password."));
        }
    }

    // Inner class to structure the response as JSON
    public static class LoginResponse {
        private String message;

        public LoginResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
