package com.eamaral.exams.user.infrastructure.repository.jpa;

import com.eamaral.exams.user.infrastructure.repository.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
