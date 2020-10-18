package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativeJpaRepository extends JpaRepository<AlternativeEntity, Long> {
}
