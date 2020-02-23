package com.amaral.exams.question.infrastructure;

import com.amaral.exams.question.infrastructure.jpa.SubjectJpaRepository;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubjectRepository {

    private final SubjectJpaRepository repository;

    @Autowired
    public SubjectRepository(SubjectJpaRepository repository) {
        this.repository = repository;
    }

    public SubjectEntity save(SubjectEntity subject){
        return repository.saveAndFlush(subject);
    }
}
