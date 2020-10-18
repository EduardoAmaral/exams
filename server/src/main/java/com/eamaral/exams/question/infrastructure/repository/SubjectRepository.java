package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.domain.port.SubjectRepositoryPort;
import com.eamaral.exams.question.infrastructure.repository.jpa.SubjectJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.SubjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class SubjectRepository implements SubjectRepositoryPort {

    private final SubjectJpaRepository repository;

    @Autowired
    public SubjectRepository(SubjectJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subject save(Subject subject) {
        return repository.saveAndFlush(SubjectEntity.from(subject)).toDomain();
    }

    @Override
    public List<Subject> findAll() {
        return repository.findAll()
                .stream()
                .map(SubjectEntity::toDomain)
                .collect(toList());
    }
}
