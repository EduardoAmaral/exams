package com.amaral.exams.question.application.dto;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Alternative;
import com.amaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO implements Serializable, Question {

    private Long id;

    @NotBlank(message = "{question.statement.required}")
    @Size(min = 4, max = 2000, message = "{question.statement.size}")
    private String statement;

    @NotNull(message = "{question.type.required}")
    private QuestionType type;

    @Size(max = 3000, message = "{question.solution.size}")
    private String solution;

    private boolean active;

    private boolean sharable;

    @Builder.Default
    @NotEmpty(message = "{question.alternatives.required}")
    private List<AlternativeDTO> alternatives = List.of();

    @NotBlank(message = "{question.answer.required}")
    private String correctAnswer;

    private String topic;

    public static QuestionDTO from(Question question){
        return QuestionDTO.builder()
                .id(question.getId())
                .statement(question.getStatement())
                .active(question.isActive())
                .solution(question.getSolution())
                .type(question.getType())
                .correctAnswer(question.getCorrectAnswer())
                .topic(question.getTopic())
                .alternatives(AlternativeDTO.from(question.getAlternatives()))
                .build();
    }

    public List<Alternative> getAlternatives(){
        return new ArrayList<>(alternatives);
    }

}

