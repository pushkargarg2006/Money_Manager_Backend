package com.MoneyManager.MoneyManager.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {

    private final WebClient webClient;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String fromEmail;

    @Value("${brevo.sender.name}")
    private String fromName;

    public EmailService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.brevo.com/v3")
            .build();
    }

    public void sendEmail(String to, String subject, String body) {

        Map<String, Object> payload = Map.of(
            "sender", Map.of(
                "email", fromEmail,
                "name", fromName
            ),
            "to", new Object[] {
                Map.of("email", to)
            },
            "subject", subject,
            "htmlContent", body
        );

        try {
            webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            System.out.println("✅ Email sent via Brevo API");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via Brevo API");
        }
    }
}
