package com.amaral.exams.question.domain.services.services;

import com.amaral.exams.configuration.exception.InvalidDataException;
import com.amaral.exams.question.domain.Question;
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

    @Override
    public List<Question> saveAll(List<Question> question) {
        return repository.saveAll(question);
    }

    @Override
    public Question update(Question question) {
        if(!repository.findById(question.getId()).getType().equals(question.getType())){
            throw new InvalidDataException("Question's type cannot be updated");
        }

        return repository.save(question);
    }
}
