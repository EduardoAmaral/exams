package com.eamaral.exams.question.infrastructure.converter;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.jpa.entity.*;

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
                    .topic(question.getTopic())
                    .subject(SubjectEntity.from(question.getSubject()))
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
                    .topic(question.getTopic())
                    .subject(SubjectEntity.from(question.getSubject()))
                    .build();
        }
        throw new InvalidDataException("{question.type.invalid}");
    }
}