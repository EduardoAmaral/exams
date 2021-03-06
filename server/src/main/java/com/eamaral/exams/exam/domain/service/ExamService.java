package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepositoryPort repository;

    public ExamService(ExamRepositoryPort repository) {
        this.repository = repository;
    }

    public void create(Exam exam) {
        exam.validateDates();
        repository.save(exam);
    }

    public List<Exam> findByUser(String currentUser) {
        return repository.findByUser(currentUser);
    }

    public List<Exam> findAvailable() {
        return repository.findAvailable();
    }

    public Exam findById(Long id, String currentUser) {
        if (StringUtils.isEmpty(id)) {
            throw new InvalidDataException("Exam's id is required");
        }

        return repository.findById(id, currentUser)
                .orElseThrow(() -> new NotFoundException(String.format("Exam %s was not found", id)));
    }

    public void delete(Long id, String currentUser) {
        Exam exam = findById(id, currentUser);

        repository.delete(exam);
    }

}
