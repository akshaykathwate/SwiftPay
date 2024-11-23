package com.paye.whitelabel.service;

import com.paye.whitelabel.Repository.TransactionRepository;
import com.paye.whitelabel.Repository.UserRepository;
import com.paye.whitelabel.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;



    public List<Transaction> getTransactionsByUsername(String username) {
        return transactionRepository.findByUsername(username);
    }

}
