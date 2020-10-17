package com.eamaral.exams.question.domain;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.QuestionType;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface Question {

    Long getId();

    String getStatement();

    QuestionType getType();

    String getSolution();

    List<Alternative> getAlternatives();

    String getCorrectAnswer();

    String getKeywords();

    Subject getSubject();

    String getAuthor();

    default void validateTypeChange(Question oldQuestion) {
        if (!oldQuestion.getType().equals(getType())) {
            throw new InvalidDataException("Question's type can't be updated");
        }
    }

    default void validateAlternatives() {
        if (!getAlternatives().stream()
                .map(Alternative::getDescription)
                .collect(toList())
                .contains(getCorrectAnswer())) {
            throw new InvalidDataException("The correct answer to the question must be one of your alternatives");
        }
    }
}
