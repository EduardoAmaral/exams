package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.exam.domain.port.ExamTemplatePort;
import com.eamaral.exams.exam.domain.port.ExamTemplateRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ExamTemplateService implements ExamTemplatePort {

    private final ExamTemplateRepositoryPort repository;

    public ExamTemplateService(ExamTemplateRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void save(ExamTemplate examTemplate) {
        repository.save(examTemplate);
    }

    @Override
    public List<ExamTemplate> findByUser(String currentUser) {
        return repository.findByUser(currentUser);
    }

    @Override
    public ExamTemplate findById(Long id, String currentUser) {
        if (StringUtils.isEmpty(id)) {
            throw new InvalidDataException("Exam's id is required");
        }

        return repository.findById(id, currentUser)
                .orElseThrow(() -> new NotFoundException(String.format("Exam %s was not found", id)));
    }

    @Override
    public void delete(Long id, String currentUser) {
        ExamTemplate examTemplateToBeDeleted = findById(id, currentUser);

        repository.delete(examTemplateToBeDeleted);
    }

}
