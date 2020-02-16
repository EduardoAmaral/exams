package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.domain.Alternative;
import lombok.*;

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
public class AlternativeData implements Alternative {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Alternative's description is required")
    private String description;

    public static List<AlternativeData> from(List<Alternative> alternatives) {
        return alternatives.stream().map(AlternativeData::from).collect(toList());
    }

    private static AlternativeData from(Alternative alternative){
        return builder()
                .id(alternative.getId())
                .description(alternative.getDescription())
                .build();
    }
}
