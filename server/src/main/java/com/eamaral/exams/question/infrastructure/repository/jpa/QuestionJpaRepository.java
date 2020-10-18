package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {

    List<QuestionEntity> findAllByAuthorId(String authorId);

    @Modifying
    @Query("UPDATE QuestionEntity q SET q.deleted = true WHERE q.id = :id")
    void softDeleteQuestion(@Param("id") Long id);

}
