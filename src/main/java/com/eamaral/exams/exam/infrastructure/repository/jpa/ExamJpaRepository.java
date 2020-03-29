package com.eamaral.exams.exam.infrastructure.repository.jpa;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamJpaRepository extends JpaRepository<ExamEntity, String> {
}

