package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.stereotype.Repository;

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
}
