package com.MoneyManager.MoneyManager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.MoneyManager.MoneyManager.reposiratory.categoryRepositery;
import com.MoneyManager.MoneyManager.DataTransfer_object.incomeDto;
import com.MoneyManager.MoneyManager.entity.CategoryEntity;
import com.MoneyManager.MoneyManager.entity.IncomeEntity;
import com.MoneyManager.MoneyManager.entity.profileEntity;
import com.MoneyManager.MoneyManager.reposiratory.incomeRepositary;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class incomeService {
    
    private final categoryRepositery categoryRepositery;
    private final incomeRepositary incomeRepositary;
    private final profileService profileService;



    public incomeDto addIncome(incomeDto Dto) {
        profileEntity profile = profileService.getCurrentProfileByEmail();
        CategoryEntity category = categoryRepositery.findById(Dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"))  ;
        IncomeEntity incomeEntity = toEntity(Dto, profile, category);
        incomeEntity = incomeRepositary.save(incomeEntity);
        return toDTO(incomeEntity);
    }
        public void deleteIncome(Long incomeId){
        profileEntity profile = profileService.getCurrentProfileByEmail();
         IncomeEntity income = incomeRepositary.findById(incomeId)
         .orElseThrow(() -> new RuntimeException("Income not found"));
         if(!income.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("You are not authorized to delete this income");
         }

        incomeRepositary.deleteById(incomeId);
    }
    public List<incomeDto> filterIncome(LocalDate startDate, LocalDate endDate, String name,Sort sort){ 
         profileEntity profile = profileService.getCurrentProfileByEmail();
        List<IncomeEntity> incomes = incomeRepositary.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase
        (profile.getId(), startDate, endDate,name,sort);
        return incomes.stream().map(this::toDTO).toList();
    }

    public List<incomeDto> gettop5IncomeForCurrUser(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        List<IncomeEntity> incomes = incomeRepositary.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return incomes.stream().map(this::toDTO).toList();
    }
    public BigDecimal totalIncomeForCurrUser(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        BigDecimal totalIncome = incomeRepositary.findTotalIncomeByProfileId(profile.getId());
        return totalIncome != null ? totalIncome : BigDecimal.ZERO;
    }

       public List<incomeDto> getCurrentMonthIncomesForCurrentProfile(){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        LocalDate starDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<IncomeEntity> incomes = incomeRepositary.findByProfileIdAndDateBetween(profile.getId(), starDate, endDate);
        return incomes.stream().map(this::toDTO).toList();
    }


    private IncomeEntity toEntity(incomeDto incomeDto,profileEntity  profile,CategoryEntity category){
        return IncomeEntity.builder()
        .name(incomeDto.getName())
        .amount(incomeDto.getAmount())
        .icon(incomeDto.getIcon())
        .date(incomeDto.getDate())
        .category(category)
        .profile(profile)
        .build();
    }
    private incomeDto toDTO(IncomeEntity incomeEntity){
        return incomeDto.builder()
        .id(incomeEntity.getId())
        .name(incomeEntity.getName())
        .amount(incomeEntity.getAmount())
        .icon(incomeEntity.getIcon())
        .date(incomeEntity.getDate())
        .categoryName(incomeEntity.getCategory().getName()!= null ? incomeEntity.getCategory().getName() : null)
        .categoryId(incomeEntity.getCategory()!= null ? incomeEntity.getCategory().getId() : null)
        .createdAt(incomeEntity.getCreatedAt())
        .updatedAt(incomeEntity.getUpdatedAt())
        .build();
    }
}
