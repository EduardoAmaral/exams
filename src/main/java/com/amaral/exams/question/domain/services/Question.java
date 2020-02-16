package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.QuestionType;
import lombok.Builder;
import lombok.Data;

public interface Question {

    Long getId();

    String getStatement();

    QuestionType getType();

    String getSolution();

    boolean isActive();

    boolean isSharable();

}
