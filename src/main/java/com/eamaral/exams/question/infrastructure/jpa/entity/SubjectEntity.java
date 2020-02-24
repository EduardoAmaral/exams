package com.eamaral.exams.question.infrastructure.jpa.entity;

import com.eamaral.exams.question.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "TB_SUBJECT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectEntity implements Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{subject.description.required}")
    private String description;

    public static SubjectEntity from(Subject subject) {
        return SubjectEntity.builder()
                .id(subject.getId())
                .description(subject.getDescription())
                .build();
    }
}
