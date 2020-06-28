package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

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
    @Column(unique = true)
    private String description;

    public static SubjectEntity from(Subject subject) {
        if (subject == null) return new SubjectEntity();

        return builder()
                .id(subject.getId())
                .description(subject.getDescription())
                .build();
    }
}
