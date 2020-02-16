package com.amaral.exams.question.application.dto;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.services.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO implements Serializable, Question {

    private Long id;

    @NotEmpty(message = "Statement is required")
    @Size(min = 4, max = 2000, message = "The statement should have between 4 and 2000 characters")
    private String statement;

    @NotNull(message = "Type is required")
    private QuestionType type;

    @Max(value = 3000, message = "The maximum value to solution is 3000 characters")
    private String solution;

    private boolean active;

    private boolean sharable;

    public static QuestionDTO from(Question question){
        return QuestionDTO.builder()
                .id(question.getId())
                .statement(question.getStatement())
                .active(question.isActive())
                .solution(question.getSolution())
                .type(question.getType())
                .build();
    }

}

