package com.eamaral.exams.question.infrastructure.repository.converter;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.*;

public class QuestionConverter {

    private QuestionConverter() {
        super();
    }

    public static QuestionEntity from(Question question) {
        if (QuestionType.MULTIPLE_CHOICES.equals(question.getType())) {
            return MultipleChoiceEntity.builder()
                    .id(question.getId())
                    .correctAnswer(question.getCorrectAnswer())
                    .active(true)
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .topic(question.getTopic())
                    .subject(SubjectEntity.from(question.getSubject()))
                    .alternatives(AlternativeEntity.from(question.getAlternatives()))
                    .author(question.getAuthor())
                    .build();
        } else {
            return TrueOrFalseEntity.builder()
                    .id(question.getId())
                    .correctAnswer(question.getCorrectAnswer())
                    .active(true)
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .topic(question.getTopic())
                    .subject(SubjectEntity.from(question.getSubject()))
                    .author(question.getAuthor())
                    .build();
        }
    }
}