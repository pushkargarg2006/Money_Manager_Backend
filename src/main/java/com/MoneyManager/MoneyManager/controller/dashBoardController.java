package com.MoneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MoneyManager.MoneyManager.service.DashboardService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class dashBoardController {
    private final DashboardService dashboardService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashboardData(){
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
    


}
