package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.eamaral.exams.exam.infrastructure.repository.ExamSpecification.authorEqualsTo;
import static com.eamaral.exams.exam.infrastructure.repository.ExamSpecification.isAvailableNow;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public class ExamRepository implements ExamRepositoryPort {

    private final ExamJpaRepository repository;

    public ExamRepository(ExamJpaRepository repository) {
        this.repository = repository;
    }


    @Override
    public Exam save(Exam exam) {
        ExamEntity examEntity = ExamEntity.from(exam);
        return repository.saveAndFlush(examEntity);
    }

    @Override
    public List<Exam> findByUser(String currentUser) {
        return new ArrayList<>(repository.findAll(
                where(authorEqualsTo(currentUser))));
    }

    @Override
    public List<Exam> findAvailable() {
        return new ArrayList<>(
                repository.findAll(isAvailableNow(LocalDateTime.now()))
        );
    }
}
