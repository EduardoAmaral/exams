package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class QuestionSpecification {

    private QuestionSpecification() {
        super();
    }

    public static Specification<QuestionEntity> hasUserId(String userId) {
        return (question, cq, cb) -> cb.equal(question.get("userId"), userId);
    }

    public static Specification<QuestionEntity> matchFilters(QuestionEntity query) {
        return (question, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(question.get("userId"), query.getUserId()));

            if (query.getStatement() != null) {
                predicates.add(cb.like(question.get("statement"), "%" + query.getStatement() + "%"));
            }

            if (query.getType() != null) {
                predicates.add(cb.equal(question.get("type"), query.getType()));
            }

            cb.asc(question.get("statement"));
            cb.asc(question.get("type"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
