package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.Exam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private boolean mockTest;

}
