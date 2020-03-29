package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.util.Collections.emptyList;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamDTO extends Exam {

    private String id;

    @NotNull(message = "{exam.template.required}")
    private ExamTemplateDTO template;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime endDateTime;

    private boolean mockTest;

    public static ExamDTO from(Exam exam) {
        ExamDTOBuilder builder = builder();

        if (exam != null) {
            builder.id(exam.getId())
                    .startDateTime(exam.getStartDateTime())
                    .endDateTime(exam.getEndDateTime())
                    .mockTest(exam.isMockTest())
                    .template(ExamTemplateDTO.from(exam.getTemplate()))
                    .build();
        }

        return builder.build();
    }

    public static ExamDTO fromExamWithoutQuestions(Exam exam) {
        ExamDTO dto = from(exam);

        return dto.toBuilder()
                .template(dto.template
                        .toBuilder()
                        .questions(emptyList())
                        .build())
                .build();
    }
}
