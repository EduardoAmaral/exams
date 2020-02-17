package com.amaral.exams.question.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
}
