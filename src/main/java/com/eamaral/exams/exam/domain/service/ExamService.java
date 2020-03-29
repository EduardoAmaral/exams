package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamPort;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService implements ExamPort {

    private final ExamRepositoryPort repository;

    public ExamService(ExamRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void create(Exam exam, String currentUser) {
        exam.validate(currentUser);
        repository.save(exam);
    }

    @Override
    public List<Exam> findByUser(String currentUser) {
        return repository.findByUser(currentUser);
    }

    @Override
    public List<Exam> findAvailable() {
        return repository.findAvailable();
    }

}
