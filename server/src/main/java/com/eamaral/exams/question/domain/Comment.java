package com.eamaral.exams.question.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Comment {

    private final Long id;

    private final String message;

    private final Long questionId;

    private final String authorId;

    private final String authorName;

    private final ZonedDateTime creationDate;
}
