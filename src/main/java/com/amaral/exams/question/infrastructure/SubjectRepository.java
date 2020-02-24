package com.amaral.exams.question.infrastructure;

import com.amaral.exams.question.domain.Subject;
import com.amaral.exams.question.domain.services.port.SubjectRepositoryPort;
import com.amaral.exams.question.infrastructure.jpa.SubjectJpaRepository;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SubjectRepository implements SubjectRepositoryPort {

    private final SubjectJpaRepository repository;

    @Autowired
    public SubjectRepository(SubjectJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subject save(Subject subject){
        return repository.saveAndFlush(SubjectEntity.from(subject));
    }

    @Override
    public List<Subject> findAll() {
        return new ArrayList<>(repository.findAll());
    }
}
