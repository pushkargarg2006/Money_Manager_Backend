package com.MoneyManager.MoneyManager.DataTransfer_object;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class categoryDto {
    private Long id;
    private Long profileId;
    private String name;
    private String icon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String type;
}
