package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Long> {
}
