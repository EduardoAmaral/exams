package com.eamaral.exams.question;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QuestionType {
    @JsonProperty("Multiple Choices")
    MULTIPLE_CHOICES,

    @JsonProperty("True Or False")
    TRUE_OR_FALSE
}
