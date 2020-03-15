package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryPort {

    List<Question> findByUser(String author);

    Optional<Question> find(String id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    List<Question> findByCriteria(Question criteria, String currentUser);

    void delete(Question question);
}
