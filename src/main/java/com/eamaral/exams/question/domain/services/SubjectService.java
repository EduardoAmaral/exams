package com.eamaral.exams.question.domain.services;

import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.domain.services.port.SubjectPort;
import com.eamaral.exams.question.domain.services.port.SubjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements SubjectPort {

    private final SubjectRepositoryPort repository;

    @Autowired
    public SubjectService(SubjectRepositoryPort repository) {
        this.repository = repository;
    }

    public Subject save(Subject subject) {
        return repository.save(subject);
    }

    public List<Subject> findAll() {
        return repository.findAll();
    }
}
