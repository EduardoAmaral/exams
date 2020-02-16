package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.exception.DataNotFoundException;
import com.amaral.exams.question.domain.services.Question;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
import com.amaral.exams.question.infrastructure.jpa.QuestionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.amaral.exams.question.infrastructure.jpa.QuestionData.from;
import static java.util.stream.Collectors.toList;

@Repository
public class QuestionRepository implements QuestionRepositoryPort {

    private final QuestionJpaRepository repository;

    @Autowired
    public QuestionRepository(QuestionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public Question findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.format("Question %d not found", id)));
    }

    @Override
    public Question save(Question question) {
        return repository.saveAndFlush(from(question));
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionData> questionsData = questions.stream().map(QuestionData::from).collect(toList());
        return new ArrayList<>(repository.saveAll(questionsData));
    }
}
