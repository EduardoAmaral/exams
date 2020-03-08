package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.domain.Alternative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ALTERNATIVE")
public class AlternativeEntity implements Alternative {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Alternative's description is required")
    private String description;

    public static List<AlternativeEntity> from(List<Alternative> alternatives) {
        if (alternatives == null) return emptyList();

        return alternatives.stream().map(AlternativeEntity::from).collect(toList());
    }

    private static AlternativeEntity from(Alternative alternative) {
        return builder()
                .id(alternative.getId())
                .description(alternative.getDescription())
                .build();
    }
}
