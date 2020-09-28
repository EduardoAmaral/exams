package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eamaral.exams.exam.infrastructure.repository.ExamSpecification.isAvailableNow;

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
        return new ArrayList<>(repository.findAllByAuthorAndDeletedIsFalse(currentUser));
    }

    @Override
    public List<Exam> findAvailable() {
        return new ArrayList<>(
                repository.findAll(isAvailableNow(ZonedDateTime.now()))
        );
    }

    @Override
    public Optional<Exam> findById(Long id, String currentUser) {
        return repository.findByIdAndAuthorAndDeletedIsFalse(id, currentUser)
                .flatMap(Optional::of);
    }

    @Override
    public void delete(Exam exam) {
        ExamEntity examToBeDeleted = ExamEntity.from(exam)
                .toBuilder()
                .deleted(true)
                .build();

        repository.save(examToBeDeleted);
    }
}
