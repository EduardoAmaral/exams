package com.eamaral.exams.question.infrastructure.repository.jpa;

import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class QuestionSpecification {

    private QuestionSpecification() {
        super();
    }

    public static Specification<QuestionEntity> matchFilters(QuestionEntity query) {
        return (question, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (shouldFilterByStatement(query)) {
                predicates.add(cb.like(cb.upper(question.get("statement")), like(query.getStatement())));
            }

            if (shouldFilterByType(query)) {
                predicates.add(cb.equal(question.get("type"), query.getType()));
            }

            if (shouldFilterByKeywords(query)) {
                predicates.add(cb.like(cb.upper(question.get("keywords")), like(query.getKeywords())));
            }

            if (shouldFilterBySubject(query)) {
                predicates.add(cb.equal(question.get("subject"), query.getSubject()));
            }

            if (shouldFilterByAuthor(query)) {
                predicates.add(cb.equal(question.get("authorId"), query.getAuthorId()));
            }

            cb.asc(question.get("statement"));
            cb.asc(question.get("type"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean shouldFilterByAuthor(QuestionEntity query) {
        return !StringUtils.isEmpty(query.getAuthorId());
    }

    private static boolean shouldFilterByStatement(QuestionEntity query) {
        return !StringUtils.isEmpty(query.getStatement());
    }

    private static boolean shouldFilterByType(QuestionEntity query) {
        return query.getType() != null;
    }

    private static boolean shouldFilterByKeywords(QuestionEntity query) {
        return !StringUtils.isEmpty(query.getKeywords());
    }

    private static boolean shouldFilterBySubject(QuestionEntity query) {
        return query.getSubject().getId() != null;
    }

    private static String like(String field) {
        return "%" + field.toUpperCase() + "%";
    }
}
