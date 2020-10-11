package com.eamaral.worker.question.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Question {

    private final Long id;

    private final String statement;

    private final String author;

    private final String keywords;

    private final String subject;

    private final QuestionType type;

}
