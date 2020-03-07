package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {
    static Specification<QuestionEntity> hasUserId(String userId) {
        return (question, cq, cb) -> cb.equal(question.get("userId"), userId);
    }

    static Specification<QuestionEntity> hasStatementLike(String statement) {
        return (question, cq, cb) -> cb.like(question.get("statement"), statement);
    }
}
