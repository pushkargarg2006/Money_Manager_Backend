package com.MoneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.RestController;

import com.MoneyManager.MoneyManager.DataTransfer_object.AuthDTO;
import com.MoneyManager.MoneyManager.DataTransfer_object.profileDTO;
import com.MoneyManager.MoneyManager.service.profileService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
public class profileController {
    private final profileService profileService;

    @PostMapping("/register")
    public ResponseEntity<profileDTO> registerProfile(@RequestBody profileDTO entity) {
        
             profileDTO newRegister = profileService.registerProfile(entity);
        
        
        return ResponseEntity.status(HttpStatus.CREATED).body(newRegister);
    

    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAcc(@RequestParam String token) {
        boolean activated = profileService.activateAccount(token);
        if(activated) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid activation token.");
        }
        
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login (@RequestBody AuthDTO authDTO) {

        try {
            if(!profileService.isAccountActive(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Account is not activated. Please check your email for the activation link."));   
            }
            Map<String,Object> response = profileService.AuthenticateAndGenrateToken(authDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("  error", e.getMessage()));

            }
        

    }

    @GetMapping("/test")
    public String Test() {
        return "success";
    }
    
}