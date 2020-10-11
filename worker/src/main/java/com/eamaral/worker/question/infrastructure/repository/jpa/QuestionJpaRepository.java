package com.eamaral.worker.question.infrastructure.repository.jpa;

import com.eamaral.worker.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

}
