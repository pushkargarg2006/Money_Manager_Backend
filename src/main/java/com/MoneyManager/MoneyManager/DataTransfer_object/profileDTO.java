package com.MoneyManager.MoneyManager.DataTransfer_object;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class profileDTO {
    
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
