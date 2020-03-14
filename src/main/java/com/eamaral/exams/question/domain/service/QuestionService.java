package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.port.QuestionPort;
import com.eamaral.exams.question.domain.port.QuestionRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService implements QuestionPort {

    private final QuestionRepositoryPort repository;

    @Autowired
    public QuestionService(QuestionRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> findByUser(String userId) {
        return repository.findByUser(userId);
    }

    @Override
    public Question find(long id) {
        return repository.find(id)
                .orElseThrow(() -> new NotFoundException(String.format("{question.not.found}", id)));
    }

    @Override
    public Question save(Question question) {
        return repository.save(question);
    }

    @Override
    public List<Question> saveAll(List<Question> question) {
        return repository.saveAll(question);
    }

    @Override
    public Question update(Question question) {
        Question oldQuestion = find(question.getId());

        question.validate(oldQuestion);

        return repository.save(question);
    }

    @Override
    public void delete(long id, String currentUserId) {
        Question question = find(id);
        repository.delete(question);
    }
}
