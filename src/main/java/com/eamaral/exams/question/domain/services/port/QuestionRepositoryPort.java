package com.eamaral.exams.question.domain.services.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryPort {

    List<Question> findAll();

    Question find(Long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findByStatement(String statement);

    void delete(Long id);
}
