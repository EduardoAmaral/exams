package com.amaral.exams.question;

public enum QuestionType {

    MULTIPLE_CHOICES("MC"),
    TRUE_OR_FALSE("TF");

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }
}
