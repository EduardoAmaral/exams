package com.eamaral.exams.question.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"description"})
public class Subject {

    private final Long id;

    private final String description;
}
