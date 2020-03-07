package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;

public interface QuestionPort {

    List<Question> findAll();

    Question find(long id);

    Question save(Question question);

    List<Question> saveAll(List<Question> question);

    Question update(Question question);

    void delete(long id);
}
