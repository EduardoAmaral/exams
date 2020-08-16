package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.domain.port.SubjectPort;
import com.eamaral.exams.question.domain.port.SubjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Subject::getDescription))
                .collect(Collectors.toList());
    }
}
