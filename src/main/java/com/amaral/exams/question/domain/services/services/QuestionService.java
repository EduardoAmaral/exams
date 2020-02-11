package com.amaral.exams.question.domain.services.services;

import com.amaral.exams.question.domain.services.Question;
import com.amaral.exams.question.domain.services.port.QuestionPort;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
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
    public List<Question> findAll() {
        return repository.findAll();
    }

    @Override
    public Question findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Question save(Question question) {
        return repository.save(question);
    }
}
