package com.eamaral.exams.exam.infrastructure.repository.jpa;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamJpaRepository extends JpaRepository<ExamEntity, String> {

    Optional<ExamEntity> findByIdAndAuthorAndActiveIsTrue(String id, String author);

    List<ExamEntity> findAllByAuthorAndActiveIsTrue(String author);

}
