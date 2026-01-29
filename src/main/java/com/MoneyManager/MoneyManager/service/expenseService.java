package com.MoneyManager.MoneyManager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.MoneyManager.MoneyManager.reposiratory.categoryRepositery;
import com.MoneyManager.MoneyManager.DataTransfer_object.expenseDto;
import com.MoneyManager.MoneyManager.entity.CategoryEntity;
import com.MoneyManager.MoneyManager.entity.expenseEntity;
import com.MoneyManager.MoneyManager.entity.profileEntity;
import com.MoneyManager.MoneyManager.reposiratory.expenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class expenseService {
    
    private final profileService profileService;
    private final categoryRepositery categoryRepositery;
    private final expenseRepository expenseRepository;

    public List<expenseDto> getCurrentMonthExpensesForCurrentProfile(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        LocalDate starDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<expenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), starDate, endDate);
        return expenses.stream().map(this::toDTO).toList();
    }
    public List<expenseDto> gettop5expensesForCurrUser(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        List<expenseEntity> expenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenses.stream().map(this::toDTO).toList();
    }
    public BigDecimal totalExpenseForCurrUser(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        BigDecimal totalExpense = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return totalExpense != null ? totalExpense : BigDecimal.ZERO;
    }
    public List<expenseDto> getExpensesByDate(Long PId,LocalDate date){
        
        List<expenseEntity> expenses = expenseRepository.findByProfileIdAndDate(PId, date);
        return expenses.stream().map(this::toDTO).toList();
    }



    public expenseDto addExpense(expenseDto Dto) {
        profileEntity profile = profileService.getCurrentProfileByEmail();
        CategoryEntity category = categoryRepositery.findById(Dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"))  ;
        expenseEntity expenseEntity = toEntity(Dto, profile, category);
        expenseEntity = expenseRepository.save(expenseEntity);
        return toDTO(expenseEntity);
    }

    public void deleteExpense(Long expenseId){
        profileEntity profile = profileService.getCurrentProfileByEmail();
         expenseEntity expense = expenseRepository.findById(expenseId)
         .orElseThrow(() -> new RuntimeException("Expense not found"));
         if(!expense.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("You are not authorized to delete this expense");
         }

        expenseRepository.deleteById(expenseId);
    }





    private expenseEntity toEntity(expenseDto expenseDto,profileEntity  profile,CategoryEntity category){
        return expenseEntity.builder()
        .name(expenseDto.getName())
        .amount(expenseDto.getAmount())
        .icon(expenseDto.getIcon())
        .date(expenseDto.getDate())
        .category(category)
        .profile(profile)
        .build();
    }
    public List<expenseDto> filterExpenses(LocalDate startDate, LocalDate endDate, String name,Sort sort){ 
         profileEntity profile = profileService.getCurrentProfileByEmail();
        List<expenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase
        (profile.getId(), startDate, endDate,name,sort);
        return expenses.stream().map(this::toDTO).toList();

    }

    private expenseDto toDTO(expenseEntity expenseEntity){
        return expenseDto.builder()
        .id(expenseEntity.getId())
        .name(expenseEntity.getName())
        .amount(expenseEntity.getAmount())
        .icon(expenseEntity.getIcon())
        .date(expenseEntity.getDate())
        .categoryName(expenseEntity.getCategory().getName()!= null ? expenseEntity.getCategory().getName() : null)
        .categoryId(expenseEntity.getCategory()!= null ? expenseEntity.getCategory().getId() : null)
        .createdAt(expenseEntity.getCreatedAt())
        .updatedAt(expenseEntity.getUpdatedAt())
        .build();
    }
        
}
