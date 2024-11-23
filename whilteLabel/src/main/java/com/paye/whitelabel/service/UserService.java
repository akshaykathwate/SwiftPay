package com.paye.whitelabel.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paye.whitelabel.Repository.UserRepository;
import com.paye.whitelabel.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user) {
        userRepository.save(user); 
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);  // Assumes you have a findByEmail method in UserRepository
    }
}


