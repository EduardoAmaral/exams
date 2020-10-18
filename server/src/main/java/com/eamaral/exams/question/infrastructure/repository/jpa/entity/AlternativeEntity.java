package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ALTERNATIVE")
public class AlternativeEntity implements Alternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Alternative's description is required")
    private String description;

    @Column(name = "QUESTION_ID")
    private Long questionId;

    public static List<AlternativeEntity> withId(List<Alternative> alternatives, Long id) {
        return CollectionUtils.emptyIfNull(alternatives).stream()
                .map(alternative -> from(alternative, id))
                .collect(toList());
    }

    public static AlternativeEntity from(Alternative alternative, Long questionId) {
        return builder()
                .id(alternative.getId())
                .description(alternative.getDescription())
                .questionId(questionId)
                .build();
    }

    public static List<AlternativeEntity> from(Question question) {
        return withId(question.getAlternatives(), question.getId());
    }
}
