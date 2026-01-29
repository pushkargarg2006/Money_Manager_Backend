package com.MoneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.MoneyManager.MoneyManager.service.expenseService;
import lombok.RequiredArgsConstructor;
import com.MoneyManager.MoneyManager.DataTransfer_object.expenseDto;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class expenseController {
    
    private final expenseService expenseService;

    @PostMapping
    public ResponseEntity<expenseDto> addExpense(@RequestBody expenseDto Dto) {
        expenseDto saved = expenseService.addExpense(Dto);
        return ResponseEntity.status(Response.SC_CREATED).body(saved);
    }


    @GetMapping
    public ResponseEntity<List<expenseDto>> getExpenses() {
        return ResponseEntity.ok(expenseService.getCurrentMonthExpensesForCurrentProfile());
    }
    @DeleteMapping("/{Id}")  
    public ResponseEntity<Void> deleteExpense(@PathVariable Long Id){
        expenseService.deleteExpense(Id);
        return ResponseEntity.noContent().build();
    }
    
}
