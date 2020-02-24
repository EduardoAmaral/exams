package com.eamaral.exams.question.infrastructure.jpa;

import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    Optional<Question> findByStatement(String statement);
}
