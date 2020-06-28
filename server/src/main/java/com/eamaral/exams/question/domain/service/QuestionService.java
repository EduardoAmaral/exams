package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.port.QuestionPort;
import com.eamaral.exams.question.domain.port.QuestionRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService implements QuestionPort {

    private final QuestionRepositoryPort repository;

    public QuestionService(QuestionRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> findByUser(String currentUser) {
        return repository.findByUser(currentUser);
    }

    @Override
    public Question find(Long id, String currentUser) {
        if (id == null) {
            throw new InvalidDataException("Question's id is required");
        }

        return repository.find(id, currentUser)
                .orElseThrow(() -> new NotFoundException(String.format("Question's %s doesn't exist or it's not accessible to the user %s", id, currentUser)));
    }

    @Override
    public void save(Question question) {
        question.validateAlternatives();

        repository.save(question);
    }

    @Override
    public List<Question> saveAll(List<Question> question) {
        return repository.saveAll(question);
    }

    @Override
    public Question update(Question question, String currentUser) {
        Question oldQuestion = find(question.getId(), currentUser);

        question.validateTypeChange(oldQuestion);
        question.validateAlternatives();

        return repository.save(question);
    }

    @Override
    public void delete(Long id, String currentUser) {
        Question question = find(id, currentUser);
        repository.delete(question);
    }

    @Override
    public List<Question> search(Question criteria, String currentUser) {
        return repository.findByCriteria(criteria, currentUser);
    }
}
