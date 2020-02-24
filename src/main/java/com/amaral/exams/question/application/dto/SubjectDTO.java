package com.amaral.exams.question.application.dto;

import com.amaral.exams.question.domain.Subject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectDTO implements Serializable, Subject {

    private Long id;

    @NotBlank(message = "{subject.description.required}")
    private String description;

    public static SubjectDTO from(Subject subject) {
        return SubjectDTO.builder()
                .id(subject.getId())
                .description(subject.getDescription())
                .build();
    }
}
