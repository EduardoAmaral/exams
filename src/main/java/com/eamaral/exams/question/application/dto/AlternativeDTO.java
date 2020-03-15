package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.domain.Alternative;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternativeDTO implements Serializable, Alternative {

    private Long id;

    private String description;

    public static List<AlternativeDTO> from(List<Alternative> alternatives) {
        List<AlternativeDTO> result = emptyList();

        if (alternatives != null) {
            result = alternatives.stream().map(AlternativeDTO::from).collect(toList());
        }

        return result;
    }

    private static AlternativeDTO from(Alternative alternative) {
        final AlternativeDTOBuilder builder = builder();

        if (alternative != null) {
            builder.id(alternative.getId())
                    .description(alternative.getDescription());
        }

        return builder.build();
    }
}
