package com.eamaral.exams.exam.infrastructure.repository.jpa;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamJpaRepository extends JpaRepository<ExamEntity, String>, JpaSpecificationExecutor<ExamEntity> {

    Optional<ExamEntity> findByIdAndAuthorAndDeletedIsFalse(Long id, String author);

    List<ExamEntity> findAllByAuthorAndDeletedIsFalse(String author);
}

