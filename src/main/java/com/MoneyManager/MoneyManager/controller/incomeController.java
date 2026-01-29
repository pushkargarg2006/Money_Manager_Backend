package com.MoneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.MoneyManager.MoneyManager.service.incomeService;

import lombok.RequiredArgsConstructor;

import com.MoneyManager.MoneyManager.DataTransfer_object.incomeDto;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class incomeController {
    private final incomeService incomeService;

    @PostMapping
    public ResponseEntity<incomeDto> addIncome(@RequestBody incomeDto Dto) {
        incomeDto saved = incomeService.addIncome(Dto);
        return ResponseEntity.status(Response.SC_CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<incomeDto>> getIncomes() {
        return ResponseEntity.ok(incomeService.getCurrentMonthIncomesForCurrentProfile());
    }

    @DeleteMapping("/{Id}")  
    public ResponseEntity<Void> deleteIncome(@PathVariable Long Id){
        incomeService.deleteIncome(Id);
        return ResponseEntity.noContent().build();
    }
    
}


