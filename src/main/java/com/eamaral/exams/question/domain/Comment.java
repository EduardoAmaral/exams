package com.eamaral.exams.question.domain;

public interface Comment {

    Long getId();

    String getMessage();

    Long getQuestionId();

    String getAuthor();
}
