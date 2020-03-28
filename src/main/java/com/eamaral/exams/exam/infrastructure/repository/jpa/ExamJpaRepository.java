package com.eamaral.exams.exam.infrastructure.repository.jpa;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamJpaRepository extends JpaRepository<ExamTemplateEntity, String> {

    Optional<ExamTemplateEntity> findByIdAndAuthorAndActiveIsTrue(String id, String author);

    List<ExamTemplateEntity> findAllByAuthorAndActiveIsTrue(String author);

}
