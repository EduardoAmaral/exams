package com.eamaral.exams.question.domain;

import com.eamaral.exams.configuration.exception.ForbiddenException;
import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.QuestionType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public interface Question {

    String getId();

    String getStatement();

    QuestionType getType();

    String getSolution();

    boolean isSharable();

    List<Alternative> getAlternatives();

    String getCorrectAnswer();

    String getTopic();

    Subject getSubject();

    String getAuthor();

    default void validateUserId(String author) {
        if (!getAuthor().equals(author)) {
            throw new ForbiddenException("Questions's user id can't be different from the question's creator");
        }
    }

    default void validate(Question oldQuestion) {
        validateUserId(oldQuestion.getAuthor());

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
