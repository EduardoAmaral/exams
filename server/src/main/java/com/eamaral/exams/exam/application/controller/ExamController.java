package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.service.ExamService;
import com.eamaral.exams.user.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/exam", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExamController {

    private final ExamService examService;

    private final UserService userService;

    public ExamController(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated ExamDTO exam) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Creating an exam to user {}", currentUserId);

        examService.create(exam.toBuilder()
                .authorId(currentUserId)
                .build());
    }

    @GetMapping
    public ResponseEntity<List<ExamDTO>> get() {
        String currentUserId = userService.getCurrentUserId();
        log.info("Getting all exams created by user {}", currentUserId);

        return ok(examService.findByUser(currentUserId)
                .stream()
                .map(ExamDTO::from)
                .collect(toList()));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ExamDTO>> getAvailable() {
        String currentUserId = userService.getCurrentUserId();
        log.info("Find available exams to user {}", currentUserId);

        return ok(examService.findAvailable()
                .stream()
                .map(ExamDTO::fromExamWithoutQuestions)
                .collect(toList()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExamDTO> getById(@PathVariable Long id) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Getting exam {} to user {}", id, currentUserId);

        return ResponseEntity.ok(
                ExamDTO.from(examService.findById(id, currentUserId)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Deleting exam {} to user {}", id, currentUserId);

        examService.delete(id, currentUserId);
    }
}
