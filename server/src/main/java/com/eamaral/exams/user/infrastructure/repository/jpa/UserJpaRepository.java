package com.eamaral.exams.user.infrastructure.repository.jpa;

import com.eamaral.exams.user.infrastructure.repository.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByIdIn(Set<String> ids);
}
