package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.question.application.dto.QuestionCriteriaDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.service.QuestionService;
import com.eamaral.exams.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping(value = "api/question", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<QuestionDTO> getById(@PathVariable("id") Long id) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Getting question {} to user {}", id, currentUserId);

        return ok(QuestionDTO.from(questionService.find(id, currentUserId)));
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> get() {
        String currentUserId = userService.getCurrentUserId();

        log.info("Getting all questions to user {}", currentUserId);

        return ok(questionService.findByUser(currentUserId)
                .stream()
                .map(QuestionDTO::from)
                .collect(toList()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated QuestionDTO question) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Saving question by user {}", currentUserId);

        question = question.toBuilder()
                .authorId(currentUserId)
                .build();
        questionService.save(question.toDomain());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/list")
    @ResponseStatus(HttpStatus.CREATED)
    public void createByList(@RequestBody @Validated List<QuestionDTO> questions) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Saving a list of {} questions to user {}", questions.size(), currentUserId);

        questions.replaceAll(question -> question.toBuilder().authorId(currentUserId).build());
        questionService.saveAll(questions.stream()
                .map(QuestionDTO::toDomain)
                .collect(toList()));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDTO> update(@RequestBody @Validated QuestionDTO question) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Updating question {} to user {}", question.getId(), currentUserId);

        return ok(QuestionDTO.from(
                questionService.update(question.toDomain(), currentUserId)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        String currentUserId = userService.getCurrentUserId();
        log.info("Deleting question {} to user {}", id, currentUserId);
        questionService.delete(id, currentUserId);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<QuestionDTO>> search(QuestionCriteriaDTO criteria) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String currentUserId = userService.getCurrentUserId();
        log.info("Search by criteria {} to user {}", mapper.writeValueAsString(criteria), currentUserId);

        return ok(questionService.search(criteria.toQuestion(), currentUserId)
                .stream()
                .map(QuestionDTO::from)
                .collect(toList()));
    }

}
