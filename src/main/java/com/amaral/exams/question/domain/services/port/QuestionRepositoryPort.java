package com.amaral.exams.question.domain.services.port;

import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.infrastructure.jpa.QuestionEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryPort {

    List<Question> findAll();

    Question findById(Long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findByStatement(String statement);
}
