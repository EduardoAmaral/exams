package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ExamRepository implements ExamRepositoryPort {

    private final ExamJpaRepository repository;

    public ExamRepository(ExamJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Exam save(Exam exam) {
        return repository.saveAndFlush(ExamEntity.from(exam));
    }

    @Override
    public Optional<Exam> findById(String id, String currentUser) {
        return repository.findByIdAndAuthorAndActiveIsTrue(id, currentUser)
                .flatMap(Optional::of);
    }

    @Override
    public List<Exam> findByUser(String currentUser) {
        return new ArrayList<>(repository.findAllByAuthorAndActiveIsTrue(currentUser));
    }

}
