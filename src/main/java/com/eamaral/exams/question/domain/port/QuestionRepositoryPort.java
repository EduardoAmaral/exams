package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryPort {

    List<Question> findAll();

    Optional<Question> find(Long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findByStatement(String statement);

    void delete(Question question);
}