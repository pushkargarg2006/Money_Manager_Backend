package com.MoneyManager.MoneyManager.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    public String fromEmail ;
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage helper = new SimpleMailMessage();
        try {
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom(fromEmail);
            mailSender.send(helper);
        } catch (Exception e) {    
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
            
        }
    }


}
