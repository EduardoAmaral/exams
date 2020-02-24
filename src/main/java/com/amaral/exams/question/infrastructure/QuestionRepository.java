package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.exception.NotFoundException;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.infrastructure.converter.QuestionConverter;
import com.amaral.exams.question.infrastructure.jpa.entity.QuestionEntity;
import com.amaral.exams.question.infrastructure.jpa.QuestionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                .orElseThrow(() -> new NotFoundException(String.format("{question.not.found}", id)));
    }

    @Override
    public Question save(Question question) {
        return repository.saveAndFlush(Objects.requireNonNull(QuestionConverter.from(question)));
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionEntity> questionsData = questions.stream().map(QuestionConverter::from).collect(toList());
        return new ArrayList<>(repository.saveAll(questionsData));
    }

    @Override
    public Optional<Question> findByStatement(String statement) {
        return repository.findByStatement(statement);
    }

    @Override
    public void delete(Long id) {
        QuestionEntity question = repository.getOne(id);
        question.setActive(false);
        repository.save(question);
    }

}
