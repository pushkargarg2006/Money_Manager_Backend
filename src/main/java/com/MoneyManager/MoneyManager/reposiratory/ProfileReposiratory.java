package com.MoneyManager.MoneyManager.reposiratory;

import com.MoneyManager.MoneyManager.entity.profileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileReposiratory extends JpaRepository<profileEntity, Long> {


    Optional<profileEntity> findByEmail(String email);
    Optional<profileEntity> findByActivationToken(String token);



}
