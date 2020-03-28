package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.exam.application.dto.ExamTemplateDTO;
import com.eamaral.exams.exam.domain.port.ExamTemplatePort;
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
@RequestMapping(value = "api/exam/template", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "ExamTemplate")
public class ExamTemplateController {

    private final ExamTemplatePort examTemplatePort;

    private final UserPort userPort;

    public ExamTemplateController(ExamTemplatePort examTemplatePort, UserPort userPort) {
        this.examTemplatePort = examTemplatePort;
        this.userPort = userPort;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated ExamTemplateDTO exam) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Saving a new exam to the user {}", currentUserId);

        exam = exam.toBuilder()
                .author(currentUserId)
                .build();
        examTemplatePort.save(exam);
    }

    @GetMapping
    public ResponseEntity<List<ExamTemplateDTO>> get() {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Getting all exams for the user {}", currentUserId);

        return ResponseEntity.ok(examTemplatePort.findByUser(currentUserId)
                .stream()
                .map(ExamTemplateDTO::from)
                .collect(toList()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExamTemplateDTO> getById(@PathVariable String id) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Getting exam {} to the user {}", id, currentUserId);

        return ResponseEntity.ok(
                ExamTemplateDTO.from(examTemplatePort.findById(id, currentUserId)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        String currentUserId = userPort.getCurrentUserId();
        log.info("Deleting exam {} to the user {}", id, currentUserId);

        examTemplatePort.delete(id, currentUserId);
    }
}
