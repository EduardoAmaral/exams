package com.eamaral.exams.question.domain;

import com.eamaral.exams.configuration.exception.ForbiddenException;
import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.QuestionType;

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

    Subject getSubject();

    String getUserId();

    default void validate(Question oldQuestion) {
        if (!oldQuestion.getUserId().equals(getUserId())) {
            throw new ForbiddenException("{question.update.user.forbidden}");
        }

        if (!oldQuestion.getType().equals(getType())) {
            throw new InvalidDataException("{question.invalid.type.update}");
        }
    }

    ;
}
