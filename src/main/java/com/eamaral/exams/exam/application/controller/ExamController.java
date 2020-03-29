package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.port.ExamPort;
import com.eamaral.exams.user.domain.port.UserPort;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/exam", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Exam")
public class ExamController {

    private final ExamPort examPort;

    private final UserPort userPort;

    public ExamController(ExamPort examPort, UserPort userPort) {
        this.examPort = examPort;
        this.userPort = userPort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated ExamDTO exam) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Creating an exam to user {}", currentUserId);

        examPort.create(exam, currentUserId);
    }
}
