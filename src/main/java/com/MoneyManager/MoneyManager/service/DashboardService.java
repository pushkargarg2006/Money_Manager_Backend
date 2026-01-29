package com.MoneyManager.MoneyManager.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.MoneyManager.MoneyManager.DataTransfer_object.expenseDto;
import com.MoneyManager.MoneyManager.DataTransfer_object.incomeDto;
import com.MoneyManager.MoneyManager.DataTransfer_object.recentTransactionDto;
import com.MoneyManager.MoneyManager.entity.profileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    

    private final incomeService incomeService;
    private final expenseService expenseService;    
    private final profileService profileService;


    public Map<String,Object> getDashboardData(){
        profileEntity  profile = profileService.getCurrentProfileByEmail();
        Map<String,Object> dashboardData = new LinkedHashMap<>();
        List<incomeDto> topIncomes = incomeService.gettop5IncomeForCurrUser();
        List<expenseDto> topExpenses = expenseService.gettop5expensesForCurrUser();
       List<recentTransactionDto> allTransactions = Stream.concat(
            topIncomes.stream().map(income -> 
                 recentTransactionDto.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
            .updatedAt(income.getUpdatedAt())
            .type("income")
            .build()
        ), topExpenses.stream().map(expense -> 
             recentTransactionDto.builder()
            .id(expense.getId())
            .profileId(profile.getId())
            .icon(expense.getIcon())
            .name(expense.getName())
            .amount(expense.getAmount())
            .date(expense.getDate())
            .createdAt(expense.getCreatedAt())
            .updatedAt(expense.getUpdatedAt())
            .type("expense")
            .build()
        )).sorted((t1, t2) -> {
            int cmp = t2.getDate().compareTo(t1.getDate());
            if(cmp == 0){
                return t2.getCreatedAt().compareTo(t1.getCreatedAt());
            }
            return cmp;
        }).collect(Collectors.toList());

        dashboardData.put("totalBalance", incomeService.totalIncomeForCurrUser()
                                            .subtract(expenseService.totalExpenseForCurrUser()));
        dashboardData.put("totalIncome", incomeService.totalIncomeForCurrUser());
        dashboardData.put("totalExpense", expenseService.totalExpenseForCurrUser());
        dashboardData.put("recent5Expenses", topExpenses);
        dashboardData.put("recent5Incomes", topIncomes);
        dashboardData.put("recentTransactions", allTransactions);
        return dashboardData;
    }
}