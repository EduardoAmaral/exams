package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.domain.Subject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectDTO implements Serializable {

    private Long id;

    @NotBlank(message = "{subject.description.required}")
    private String description;

    public static SubjectDTO from(Subject subject) {
        final SubjectDTOBuilder builder = builder();

        if (subject != null) {
            builder.id(subject.getId())
                    .description(subject.getDescription());
        }

        return builder.build();
    }

    public Subject toDomain() {
        return Subject.builder()
                .id(getId())
                .description(getDescription())
                .build();
    }
}
