package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.port.ExamPort;
import com.eamaral.exams.user.domain.port.UserPort;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated ExamDTO exam) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Saving a new exam to the user {}", currentUserId);

        exam = exam.toBuilder()
                .author(currentUserId)
                .build();
        examPort.save(exam);
    }

    @GetMapping
    public ResponseEntity<List<ExamDTO>> get() {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Getting all exams for the user {}", currentUserId);

        return ResponseEntity.ok(examPort.findByUser(currentUserId)
                .stream()
                .map(ExamDTO::from)
                .collect(toList()));
    }
}
