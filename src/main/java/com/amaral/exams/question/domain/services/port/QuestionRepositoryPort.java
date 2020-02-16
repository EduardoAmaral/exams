package com.amaral.exams.question.domain.services.port;

import com.amaral.exams.question.domain.Question;

import java.util.List;

public interface QuestionRepositoryPort {

    List<Question> findAll();

    Question findById(Long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);
}
