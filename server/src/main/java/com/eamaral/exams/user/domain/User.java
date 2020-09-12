package com.eamaral.exams.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private final String id;

    private final String email;

    private final String name;

    private final String surname;

    private final String picture;

    public String getFullName() {
        return String.format("%s %s", getName(), getSurname());
    }
}
