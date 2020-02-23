package com.amaral.exams.question.domain;

import com.amaral.exams.question.QuestionType;

import java.util.List;

public interface Question {

    Long getId();

    String getStatement();

    QuestionType getType();

    String getSolution();

    boolean isActive();

    boolean isSharable();

    List<Alternative> getAlternatives();

    String getCorrectAnswer();

    String getTopic();
}
