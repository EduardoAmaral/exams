package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.exam.domain.port.ExamTemplateRepositoryPort;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamTemplateEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ExamTemplateRepository implements ExamTemplateRepositoryPort {

    private final ExamJpaRepository repository;

    public ExamTemplateRepository(ExamJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExamTemplate save(ExamTemplate examTemplate) {
        return repository.saveAndFlush(ExamTemplateEntity.from(examTemplate));
    }

    @Override
    public Optional<ExamTemplate> findById(String id, String currentUser) {
        return repository.findByIdAndAuthorAndActiveIsTrue(id, currentUser)
                .flatMap(Optional::of);
    }

    @Override
    public List<ExamTemplate> findByUser(String currentUser) {
        return new ArrayList<>(repository.findAllByAuthorAndActiveIsTrue(currentUser));
    }

    @Override
    public void delete(ExamTemplate examTemplate) {
        ExamTemplateEntity examToBeDeleted = ExamTemplateEntity.from(examTemplate)
                .toBuilder()
                .active(false)
                .build();
        repository.save(examToBeDeleted);
    }

}
