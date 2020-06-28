package com.eamaral.exams.exam.domain;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.question.domain.Question;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Exam {

    public abstract Long getId();

    public abstract String getTitle();

    public abstract LocalDateTime getStartDateTime();

    public abstract LocalDateTime getEndDateTime();

    public abstract boolean isMockTest();

    public abstract String getAuthor();

    public abstract List<Question> getQuestions();

    public void validate() {
        if (!isMockTest()) {
            validateDates();
        }
    }

    private void validateDates() {
        if (getStartDateTime() == null || getEndDateTime() == null) {
            throw new InvalidDataException("Dates are required when not a mock test");
        }

        if (getEndDateTime().isBefore(getStartDateTime())) {
            throw new InvalidDataException("The start time must be before the end time");
        }

        if (getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Couldn't create exam starting in the past");
        }

        if (getStartDateTime().plusMinutes(30).isAfter(getEndDateTime())) {
            throw new InvalidDataException("The exam duration must be at least 30 minutes");
        }
    }
}
