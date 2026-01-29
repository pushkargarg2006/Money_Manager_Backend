package com.MoneyManager.MoneyManager.DataTransfer_object;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class recentTransactionDto {
    private Long id;
    private Long profileId;
    private String name;
    private String icon;
    private BigDecimal  amount;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String type;


}
