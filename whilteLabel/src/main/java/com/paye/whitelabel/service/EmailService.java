package com.paye.whitelabel.service;

import com.paye.whitelabel.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String messageSender;

    @Autowired
    private JavaMailSender mailSender;

    public void sendTransactionEmail(Transaction transaction) {
        String to = "akshaykathwate1421@gmail.com";
        String subject = "SwiftPay";
        String body = formatTransactionEmail(transaction);

        // Send email
        sendEmail(to, subject, body);
    }

    private String formatTransactionEmail(Transaction transaction) {
        return "Dear " + transaction.getUsername() + ",\n\n" +
                "Your recent transaction of " + transaction.getAmount() + " " + transaction.getCurrency() +
                " was " + (transaction.isSuccess() ? "successful." : "unsuccessful.") + "\n\n" +
                "Thank you for using our service.\n\nBest regards,\nSwiftPay.";
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(messageSender);
        mailSender.send(message);
    }
}
