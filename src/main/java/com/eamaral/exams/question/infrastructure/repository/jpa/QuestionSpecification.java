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

            if (query.getStatement() != null) {
                predicates.add(cb.like(question.get("statement"), like(query.getStatement())));
            }

            if (query.getType() != null) {
                predicates.add(cb.equal(question.get("type"), query.getType()));
            }

            if (query.getTopic() != null) {
                predicates.add(cb.like(question.get("topic"), like(query.getTopic())));
            }

            if(query.getSubject().getId() != null){
                predicates.add(cb.equal(question.get("subject"), query.getSubject()));
            }

            Predicate userPredicate = cb.equal(question.get("userId"), query.getUserId());
            Predicate sharablePredicate = cb.isTrue(question.get("sharable"));
            predicates.add(cb.or(userPredicate, sharablePredicate));

            cb.asc(question.get("statement"));
            cb.asc(question.get("type"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String like(String field) {
        return "%" + field + "%";
    }
}
