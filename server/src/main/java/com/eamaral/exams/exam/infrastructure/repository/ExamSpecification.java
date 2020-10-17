package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class ExamSpecification {
    private ExamSpecification() {
        super();
    }

    public static Specification<ExamEntity> isAvailableNow(ZonedDateTime currentDateTime) {
        return (exam, cq, cb) -> cb.and(
                cb.greaterThanOrEqualTo(exam.get("endDateTime"), currentDateTime),
                cb.lessThanOrEqualTo(exam.get("startDateTime"), currentDateTime));
    }
}
