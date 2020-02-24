package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.domain.Subject;
import com.amaral.exams.question.domain.services.port.SubjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    private final SubjectRepositoryPort repository;

    @Autowired
    public SubjectService(SubjectRepositoryPort repository) {
        this.repository = repository;
    }

    public Subject save(Subject subject) {
        return repository.save(subject);
    }
}
