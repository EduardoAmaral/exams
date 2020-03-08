package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {
    static Specification<QuestionEntity> hasUserId(String userId) {
        return (question, cq, cb) -> cb.equal(question.get("userId"), userId);
    }

    static Specification<QuestionEntity> matchFilters(QuestionEntity query) {
        return (question, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(question.get("userId"), query.getUserId()));

            if (query.getStatement() != null) {
                predicates.add(cb.like(question.get("statement"), "%" + query.getStatement() + "%"));
            }

            if (query.getType() != null) {
                predicates.add(cb.equal(question.get("type"), query.getType()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
