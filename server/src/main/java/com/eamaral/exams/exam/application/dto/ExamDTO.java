package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
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

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime startDateTime;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime endDateTime;

    private String authorId;

    @NotEmpty(message = "{exam.questions.required}")
    private List<QuestionDTO> questions;

    public static ExamDTO from(Exam exam) {
        ExamDTOBuilder builder = builder();

        if (exam != null) {
            builder.id(exam.getId())
                    .startDateTime(exam.getStartDateTime())
                    .endDateTime(exam.getEndDateTime())
                    .authorId(exam.getAuthorId())
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
