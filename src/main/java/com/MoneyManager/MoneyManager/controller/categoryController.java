package com.MoneyManager.MoneyManager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MoneyManager.MoneyManager.DataTransfer_object.categoryDto;
import com.MoneyManager.MoneyManager.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class categoryController {
    private final CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<categoryDto> saveCategory(@RequestBody categoryDto categoryDto){
        categoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED ).body(savedCategory);
         
    }
    @GetMapping()
    public ResponseEntity<List<categoryDto>> getAllCategories() {
        List<categoryDto> categories = categoryService.getAllCategoriesForCurrentProfile();
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/{type}")
    public ResponseEntity<List<categoryDto>> getCategoriesByType(@PathVariable String type) {
        List<categoryDto> categories = categoryService.getCategoriesByTypeForCurrentProfile(type);
        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{CategoryId}")
    public ResponseEntity<categoryDto> updateCategory(@PathVariable Long CategoryId, @RequestBody categoryDto categoryDto){
        categoryDto updatedCategory = categoryService.updateCategory(CategoryId, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

}
    