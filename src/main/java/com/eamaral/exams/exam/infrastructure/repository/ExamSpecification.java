package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;

public class ExamSpecification {
    private ExamSpecification() {
        super();
    }

    public static Specification<ExamEntity> authorEqualsTo(String author) {
        return (exam, cq, cb) -> cb.equal(exam.get("template").get("author"), author);
    }

    public static Specification<ExamEntity> isAvailableNow(LocalDateTime currentDateTime) {
        return (exam, cq, cb) -> {
            Predicate availableByDate = cb.and(
                    cb.greaterThanOrEqualTo(exam.get("endDateTime"), currentDateTime),
                    cb.lessThanOrEqualTo(exam.get("startDateTime"), currentDateTime));

            Predicate mockTest = cb.equal(exam.get("mockTest"), true);

            return cb.or(availableByDate, mockTest);
        };
    }
}
