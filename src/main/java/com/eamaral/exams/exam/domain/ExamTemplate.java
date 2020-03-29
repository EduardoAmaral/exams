package com.eamaral.exams.exam.domain;

import com.eamaral.exams.question.domain.Question;

import java.util.List;

public interface ExamTemplate {

    Long getId();

    String getTitle();

    List<Question> getQuestions();

    String getAuthor();
}
