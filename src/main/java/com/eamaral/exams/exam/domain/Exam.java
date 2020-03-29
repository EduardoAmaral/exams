package com.eamaral.exams.exam.domain;

import com.eamaral.exams.configuration.exception.ForbiddenException;
import com.eamaral.exams.configuration.exception.InvalidDataException;

import java.time.LocalDateTime;

public abstract class Exam {

    public abstract String getId();

    public abstract ExamTemplate getTemplate();

    public abstract LocalDateTime getStartDateTime();

    public abstract LocalDateTime getEndDateTime();

    public abstract boolean isMockTest();

    public void validate(String currentUser) {
        validateTemplate(currentUser);

        if (!isMockTest()) {
            validateDates();
        }
    }

    private void validateTemplate(String currentUser) {
        if (getTemplate() == null) {
            throw new InvalidDataException("Exam's template is required");
        }

        if (!getTemplate().getAuthor().equals(currentUser)) {
            throw new ForbiddenException("Unable to use other users' exam template");
        }
    }

    private void validateDates() {
        if (getStartDateTime() == null || getEndDateTime() == null) {
            throw new InvalidDataException("Dates are required when not a mock test");
        }

        if (getEndDateTime().isBefore(getStartDateTime())) {
            throw new InvalidDataException("The start time must be before the end time");
        }

        if(getStartDateTime().isBefore(LocalDateTime.now())){
            throw new InvalidDataException("Couldn't create exam starting in the past");
        }

        if (getStartDateTime().plusMinutes(30).isAfter(getEndDateTime())) {
            throw new InvalidDataException("The exam duration must be at least 30 minutes");
        }
    }
}
