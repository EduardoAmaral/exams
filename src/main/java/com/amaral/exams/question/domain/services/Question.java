package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.QuestionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Question {

    private Long id;

    private String statement;

    private QuestionType type;

    private String solution;

    private boolean active;

    private boolean sharable;

}
