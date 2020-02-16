package com.amaral.exams.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuestionTypeException extends RuntimeException {
    public InvalidQuestionTypeException(){
        super("Question type informed is invalid");
    }
}
