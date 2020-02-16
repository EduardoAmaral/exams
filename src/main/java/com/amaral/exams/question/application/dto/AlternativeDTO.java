package com.amaral.exams.question.application.dto;

import com.amaral.exams.question.domain.Alternative;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
        return alternatives.stream().map(AlternativeDTO::from).collect(toList());
    }

    private static AlternativeDTO from(Alternative alternative){
        return builder()
                .id(alternative.getId())
                .description(alternative.getDescription())
                .build();
    }
}
