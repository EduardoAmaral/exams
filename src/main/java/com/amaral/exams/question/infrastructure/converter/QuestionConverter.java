package com.amaral.exams.question.infrastructure.converter;

import com.amaral.exams.configuration.exception.InvalidDataException;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.infrastructure.jpa.AlternativeEntity;
import com.amaral.exams.question.infrastructure.jpa.MultipleChoiceEntity;
import com.amaral.exams.question.infrastructure.jpa.QuestionEntity;
import com.amaral.exams.question.infrastructure.jpa.TrueOrFalseEntity;

public class QuestionConverter {

    private QuestionConverter(){
        super();
    }

    public static QuestionEntity from(Question question){
        if(QuestionType.MULTIPLE_CHOICES.equals(question.getType())) {
            return MultipleChoiceEntity.builder()
                    .id(question.getId())
                    .active(question.isActive())
                    .correctAnswer(question.getCorrectAnswer())
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .alternatives(AlternativeEntity.from(question.getAlternatives()))
                    .build();
        } else if(QuestionType.TRUE_OR_FALSE.equals(question.getType())) {
            return TrueOrFalseEntity.builder()
                    .id(question.getId())
                    .active(question.isActive())
                    .correctAnswer(question.getCorrectAnswer())
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .build();
        }
        throw new InvalidDataException("Question type informed is invalid");
    }
}