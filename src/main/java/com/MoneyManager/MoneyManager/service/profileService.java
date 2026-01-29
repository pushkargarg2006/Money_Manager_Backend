package com.MoneyManager.MoneyManager.service;
import com.MoneyManager.MoneyManager.DataTransfer_object.AuthDTO;
import com.MoneyManager.MoneyManager.DataTransfer_object.profileDTO;
import com.MoneyManager.MoneyManager.entity.profileEntity;
import com.MoneyManager.MoneyManager.reposiratory.ProfileReposiratory;
import com.MoneyManager.MoneyManager.util.JwtUtil;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class profileService {
    
final ProfileReposiratory profileReposiratory;
private final EmailService emailService;
private final PasswordEncoder passwordEncoder;
private final AuthenticationManager authenticationManager;
private final JwtUtil jwtUtil;


@Value("${app.activation.url}")
private  String url;
public profileDTO registerProfile(profileDTO prod){

    profileEntity entity = toEntity(prod);
    entity.setActivationToken(UUID.randomUUID().toString());
    entity = profileReposiratory.save(entity);
    //sending activation email


    String link =  url + "Money_Manager/activate?token=" + entity.getActivationToken();
    String subject = "Activate your MoneyManager account";
    String body = "Dear " + entity.getFullName() + ",\n\nPlease click the following link to activate your account:\n" + link + "\n\nThank you!";    
    emailService.sendEmail(entity.getEmail(),subject,body);
    return toDTO(entity);
}

public profileEntity toEntity(profileDTO dto){
    profileEntity entity = profileEntity.builder()
    .id(dto.getId())
    .fullName(dto.getFullName())
    .email(dto.getEmail())
    .password(passwordEncoder.encode(dto.getPassword()))
    .imgUrl(dto.getImgUrl())
    .build();
    return entity;

    }


    public profileDTO toDTO(profileEntity entity){
        profileDTO dto = profileDTO.builder()
        .id(entity.getId())
        .fullName(entity.getFullName())
        .email(entity.getEmail())
        .imgUrl(entity.getImgUrl())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
        return dto;

    }


    public boolean activateAccount(String token) {
        profileEntity entity = profileReposiratory.findByActivationToken(token).orElse(null);
        if(entity == null) {
            return false;
        }
        entity.setIsActive(true);
        profileReposiratory.save(entity);
        return true;
    }
    public boolean isAccountActive(String email) {
        profileEntity entity = profileReposiratory.findByEmail(email).orElse(null);
        if(entity == null) {
            return false;
        }
        return entity.getIsActive();
    }

    public profileEntity getCurrentProfileByEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileReposiratory.findByEmail(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email :" + authentication.getName()));
    }
    public profileDTO getPublicProfile(String email) {
        profileEntity entity = null;
        if(email == null) {
            entity =   getCurrentProfileByEmail();

        }
        else {
            entity = profileReposiratory.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email :" + email));
        }
        return profileDTO.builder()
        .id(entity.getId())
        .fullName(entity.getFullName())
        .email(entity.getEmail())
        .imgUrl(entity.getImgUrl())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
    }

    public Map<String, Object> AuthenticateAndGenrateToken(AuthDTO authDTO) {
        

        try{
            authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    authDTO.getEmail(), authDTO.getPassword()
                )
            );
            final String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                "Token",token,
                "user",getPublicProfile(authDTO.getEmail())
            );
        }catch(Exception e){
            throw new RuntimeException("Invalid Email or password");
        }
    }

    

}