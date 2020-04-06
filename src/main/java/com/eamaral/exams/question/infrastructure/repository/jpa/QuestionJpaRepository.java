package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {

    Optional<QuestionEntity> findByIdAndAuthorOrSharedIsTrueAndDeletedIsFalse(Long id, String author);

    List<QuestionEntity> findAllByAuthorAndDeletedIsFalse(String author);

}
