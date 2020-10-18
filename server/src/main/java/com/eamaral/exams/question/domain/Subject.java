package com.eamaral.exams.question.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Subject {

    private final Long id;

    @EqualsAndHashCode.Include
    private final String description;
}
