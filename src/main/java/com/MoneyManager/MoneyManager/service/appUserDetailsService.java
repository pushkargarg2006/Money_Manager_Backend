package com.MoneyManager.MoneyManager.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.MoneyManager.MoneyManager.entity.profileEntity;
import com.MoneyManager.MoneyManager.reposiratory.ProfileReposiratory;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class appUserDetailsService implements UserDetailsService {
    private final ProfileReposiratory profileReposiratory;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        profileEntity user = profileReposiratory.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(Collections.emptyList())
        .build();

        
    }

   
}
