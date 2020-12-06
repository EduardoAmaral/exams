package com.eamaral.exams.question.domain;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Question {

    private final Long id;

    private final String statement;

    private final QuestionType type;

    private final String solution;

    private final List<Alternative> alternatives;

    private final String correctAnswer;

    private final String keywords;

    private final Subject subject;

    private final String authorId;

    public void validateTypeChange(Question oldQuestion) {
        if (!oldQuestion.getType().equals(getType())) {
            throw new InvalidDataException("Question's type can't be updated");
        }
    }

    public void validateAlternatives() {
        if (!getAlternatives().stream()
                .map(Alternative::getDescription)
                .collect(toList())
                .contains(getCorrectAnswer())) {
            throw new InvalidDataException("The correct answer to the question must be one of your alternatives");
        }

        final List<String> alternativesDescription = getAlternatives().stream()
                .map(Alternative::getDescription)
                .collect(toList());
        if (alternativesDescription.stream()
                .anyMatch(description -> Collections.frequency(alternativesDescription, description) > 1)) {
            throw new InvalidDataException("The question cannot have duplicated alternatives");
        }
    }
}
