package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO implements Serializable, Question {

    private Long id;

    @NotBlank(message = "{question.statement.required}")
    @Size(max = 2000, message = "{question.statement.size}")
    private String statement;

    @NotNull(message = "{question.type.required}")
    private QuestionType type;

    @Size(max = 3000, message = "{question.solution.size}")
    private String solution;

    private boolean shared;

    @Builder.Default
    @NotEmpty(message = "{question.alternatives.required}")
    private List<AlternativeDTO> alternatives = List.of();

    @NotBlank(message = "{question.answer.required}")
    private String correctAnswer;

    @Size(max = 255, message = "{question.keywords.size}")
    private String keywords;

    @NotNull(message = "{question.subject.required}")
    private SubjectDTO subject;

    private String authorId;

    public static QuestionDTO from(Question question) {
        final QuestionDTOBuilder builder = builder();

        if (question != null) {
            builder.id(question.getId())
                    .statement(question.getStatement())
                    .solution(question.getSolution())
                    .type(question.getType())
                    .correctAnswer(question.getCorrectAnswer())
                    .keywords(question.getKeywords())
                    .alternatives(AlternativeDTO.from(question.getAlternatives()))
                    .subject(SubjectDTO.from(question.getSubject()))
                    .authorId(question.getAuthorId());
        }

        return builder.build();
    }

    public static List<QuestionDTO> from(List<Question> questions) {
        return emptyIfNull(questions).stream()
                .map(QuestionDTO::from)
                .collect(toList());
    }

    @Override
    public List<Alternative> getAlternatives() {
        return new ArrayList<>(alternatives);
    }

}

