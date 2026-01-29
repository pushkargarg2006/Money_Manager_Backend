package com.MoneyManager.MoneyManager.reposiratory;
import com.MoneyManager.MoneyManager.entity.expenseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface expenseRepository  extends JpaRepository<expenseEntity,Long> {
    

   List<expenseEntity>  findByProfileIdOrderByDateDesc(Long profileId);

   List<expenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

   @Query("SELECT SUM(e.amount) FROM expenseEntity e WHERE e.profile.id = :profileId")
   BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<expenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
    Long profileId,LocalDate startDate,LocalDate endDate,String name,Sort sort
);  

   List<expenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate); 

   List<expenseEntity> findByProfileIdAndDate(Long profileId,LocalDate date);

}