package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryPort {

    List<Question> findByUser(String userId);

    Optional<Question> find(Long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    List<Question> findByFilter(Question question);

    void delete(Question question);
}
