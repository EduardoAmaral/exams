package com.amaral.exams.question.infrastructure.converter;

import com.amaral.exams.configuration.exception.InvalidQuestionTypeException;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.infrastructure.jpa.AlternativeData;
import com.amaral.exams.question.infrastructure.jpa.MultipleChoiceData;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
import com.amaral.exams.question.infrastructure.jpa.TrueOrFalseData;

public class QuestionConverter {

    private QuestionConverter(){
        super();
    }

    public static QuestionData from(Question question){
        if(QuestionType.MULTIPLE_CHOICES.equals(question.getType())) {
            return MultipleChoiceData.builder()
                    .id(question.getId())
                    .active(question.isActive())
                    .correctAnswer(question.getCorrectAnswer())
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .alternatives(AlternativeData.from(question.getAlternatives()))
                    .build();
        } else if(QuestionType.TRUE_OR_FALSE.equals(question.getType())) {
            return TrueOrFalseData.builder()
                    .id(question.getId())
                    .active(question.isActive())
                    .correctAnswer(question.getCorrectAnswer())
                    .sharable(question.isSharable())
                    .solution(question.getSolution())
                    .statement(question.getStatement())
                    .type(question.getType())
                    .build();
        }
        throw new InvalidQuestionTypeException();
    }
}