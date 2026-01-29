package com.MoneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Controller {
    
    @GetMapping({"/health", "/status"})
    public String healthCheck() {
        return "Money Manager Application is running!";
    }
}
