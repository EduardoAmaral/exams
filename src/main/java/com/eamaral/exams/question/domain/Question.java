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

    default void validateUserId(String userId){
        if (!getUserId().equals(userId)) {
            throw new ForbiddenException("{question.update.user.forbidden}");
        }
    }

    default void validate(Question oldQuestion) {
        validateUserId(oldQuestion.getUserId());

        if (!oldQuestion.getType().equals(getType())) {
            throw new InvalidDataException("{question.invalid.type.update}");
        }
    }

    ;
}
