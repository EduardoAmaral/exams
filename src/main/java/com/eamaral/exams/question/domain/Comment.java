package com.eamaral.exams.question.domain;

import java.time.ZonedDateTime;

public interface Comment {

    Long getId();

    String getMessage();

    Long getQuestionId();

    String getAuthor();

    ZonedDateTime getCreationDate();
}
