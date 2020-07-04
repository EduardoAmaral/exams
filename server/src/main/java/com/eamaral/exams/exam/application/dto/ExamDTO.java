package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamDTO extends Exam {

    private Long id;

    @NotBlank(message = "{exam.title.required}")
    private String title;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    private boolean mockTest;

    private String author;

    @NotEmpty(message = "{exam.questions.required}")
    private List<QuestionDTO> questions;

    public static ExamDTO from(Exam exam) {
        ExamDTOBuilder builder = builder();

        if (exam != null) {
            builder.id(exam.getId())
                    .startDateTime(exam.getStartDateTime())
                    .endDateTime(exam.getEndDateTime())
                    .mockTest(exam.isMockTest())
                    .author(exam.getAuthor())
                    .title(exam.getTitle())
                    .questions(QuestionDTO.from(exam.getQuestions()))
                    .build();
        }

        return builder.build();
    }

    public static ExamDTO fromExamWithoutQuestions(Exam exam) {
        ExamDTO dto = from(exam);

        return dto.toBuilder()
                .questions(emptyList())
                .build();
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(emptyIfNull(questions));
    }
}
