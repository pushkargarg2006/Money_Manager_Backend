package com.MoneyManager.MoneyManager.DataTransfer_object;

import java.time.LocalDate;

import lombok.Data;

@Data
public class filterDto {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String sortFeild;
    private String sortOrder;
}
