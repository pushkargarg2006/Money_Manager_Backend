package com.MoneyManager.MoneyManager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.MoneyManager.MoneyManager.reposiratory.categoryRepositery;
import com.MoneyManager.MoneyManager.DataTransfer_object.categoryDto;
import com.MoneyManager.MoneyManager.entity.CategoryEntity;
import com.MoneyManager.MoneyManager.entity.profileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final profileService profileService;
    private final categoryRepositery categoryRepositery;


    private CategoryEntity toEntity(categoryDto categoryDto,profileEntity profile){


        return CategoryEntity.builder()
        .name(categoryDto.getName())
        .type(categoryDto.getType())
        .profile(profile)
        .icon(categoryDto.getIcon())
        .build();
    }


    private categoryDto toDTO(CategoryEntity categoryEntity){
        return categoryDto.builder()
        .id(categoryEntity.getId())
        .name(categoryEntity.getName())
        .type(categoryEntity.getType())
        .icon(categoryEntity.getIcon())
        .createdAt(categoryEntity.getCreatedAt())
        .updatedAt(categoryEntity.getUpdatedAt())
        .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
        .build();
    }


public List<categoryDto> getAllCategoriesForCurrentProfile(){
    profileEntity profile = profileService.getCurrentProfileByEmail();
    List<CategoryEntity> categories = categoryRepositery.findByProfileId(profile.getId());
    return categories.stream().map(this::toDTO).toList();
}


    public categoryDto saveCategory(categoryDto categoryDto){
        profileEntity profile = profileService.getCurrentProfileByEmail();

        if(categoryRepositery.existsByNameAndProfileId( categoryDto.getName(),profile.getId())){
            throw new RuntimeException( "Category already exists");
        }
        CategoryEntity categoryEntity = toEntity(categoryDto,profile);
        categoryEntity = categoryRepositery.save(categoryEntity);
        return toDTO(categoryEntity);
    }
    public categoryDto updateCategory(Long categoryId, categoryDto categoryDto){
        profileEntity profile = profileService.getCurrentProfileByEmail();
        CategoryEntity categoryEntity = categoryRepositery.findByIdAndProfileId(categoryId, profile.getId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryEntity.setName(categoryDto.getName());
        // categoryEntity.setType(categoryDto.getType());
        categoryEntity.setIcon(categoryDto.getIcon());

        categoryEntity = categoryRepositery.save(categoryEntity);
        return toDTO(categoryEntity);

    }




    public List<categoryDto> getCategoriesByTypeForCurrentProfile(String type) {
    
        profileEntity profile = profileService.getCurrentProfileByEmail();
        List<CategoryEntity> categories = categoryRepositery.findByTypeAndProfileId(type, profile.getId());
        return categories.stream().map(this::toDTO).toList();

    }
    
}
