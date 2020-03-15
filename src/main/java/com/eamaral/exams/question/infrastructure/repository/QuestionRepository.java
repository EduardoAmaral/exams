package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.port.QuestionRepositoryPort;
import com.eamaral.exams.question.infrastructure.repository.converter.QuestionConverter;
import com.eamaral.exams.question.infrastructure.repository.jpa.QuestionJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.eamaral.exams.question.infrastructure.repository.jpa.QuestionSpecification.hasUserId;
import static com.eamaral.exams.question.infrastructure.repository.jpa.QuestionSpecification.matchFilters;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public class QuestionRepository implements QuestionRepositoryPort {

    private final QuestionJpaRepository repository;

    @Autowired
    public QuestionRepository(QuestionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> findByUser(String author) {
        return new ArrayList<>(
                repository.findAll(
                        where(hasUserId(author))));
    }

    @Override
    public Optional<Question> find(String id) {
        Optional<QuestionEntity> question = repository
                .findById(id);
        return question.flatMap(Optional::of);
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
    public List<Question> findByCriteria(Question criteria, String currentUser) {
        QuestionEntity query = QuestionConverter.from(criteria);

        return new ArrayList<>(repository.findAll(
                where(matchFilters(query, currentUser))));
    }

    @Override
    public void delete(Question question) {
        QuestionEntity questionEntity = QuestionConverter.from(question);
        questionEntity.setActive(false);
        repository.save(questionEntity);
    }

}
