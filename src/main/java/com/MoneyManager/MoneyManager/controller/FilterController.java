package com.MoneyManager.MoneyManager.controller;

import com.MoneyManager.MoneyManager.DataTransfer_object.expenseDto;
import com.MoneyManager.MoneyManager.DataTransfer_object.filterDto;
import com.MoneyManager.MoneyManager.DataTransfer_object.incomeDto;
import com.MoneyManager.MoneyManager.service.expenseService;
import com.MoneyManager.MoneyManager.service.incomeService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {

    private final incomeService incomeService;
    private final expenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody filterDto filterDto) {

        LocalDate startDate = filterDto.getStartDate() != null
                ? filterDto.getStartDate()
                : LocalDate.of(1970, 1, 1);

        LocalDate endDate = filterDto.getEndDate() != null
                ? filterDto.getEndDate()
                : LocalDate.now();

        String name = filterDto.getName() != null
                ? filterDto.getName()
                : "";

        String sortField = filterDto.getSortFeild() != null
                ? filterDto.getSortFeild()
                : "date";

        Sort.Direction direction =
                "desc".equalsIgnoreCase(filterDto.getSortOrder())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        if (filterDto.getType() == null) {
            return ResponseEntity.badRequest().body("Type is required (income / expense)");
        }

        if (filterDto.getType().equalsIgnoreCase("income")) {

            List<incomeDto> filteredIncomes =
                    incomeService.filterIncome(startDate, endDate, name, sort);

            return ResponseEntity.ok(filteredIncomes);

        } else if (filterDto.getType().equalsIgnoreCase("expense")) {

            List<expenseDto> filteredExpenses =
                    expenseService.filterExpenses(startDate, endDate, name, sort);

            return ResponseEntity.ok(filteredExpenses);

        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid type. Must be 'income' or 'expense'");
        }
    }
}
