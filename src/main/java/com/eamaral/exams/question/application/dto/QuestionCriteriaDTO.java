package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionCriteriaDTO {

    private String statement;

    private QuestionType type;

    private String topic;

    private Long subject;

    private String author;

    public Question toQuestion() {
        return QuestionDTO.builder()
                .statement(statement)
                .type(type)
                .topic(topic)
                .author(author)
                .subject(SubjectDTO.builder()
                        .id(subject)
                        .build())
                .build();
    }
}
