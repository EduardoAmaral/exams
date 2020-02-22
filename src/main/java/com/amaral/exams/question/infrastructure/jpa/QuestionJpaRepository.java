package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    Optional<Question> findByStatement(String statement);
}
