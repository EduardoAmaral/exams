package com.eamaral.worker.question.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    MULTIPLE_CHOICES("Multiple Choices"),

    TRUE_OR_FALSE("True or False");

    private final String description;

}
