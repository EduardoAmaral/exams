package com.amaral.exams.question.domain.services.port;

import com.amaral.exams.question.domain.services.Question;

import java.util.List;

public interface QuestionPort {

    List<Question> findAll();

    Question findById(long id);

    Question save(Question question);
}
