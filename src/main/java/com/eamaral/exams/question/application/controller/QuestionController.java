package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.question.application.dto.QuestionCriteriaDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.port.QuestionPort;
import com.eamaral.exams.user.domain.port.UserPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/question", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Question")
public class QuestionController {

    private final QuestionPort questionPort;
    private final UserPort userPort;

    public QuestionController(QuestionPort questionPort, UserPort userPort) {
        this.questionPort = questionPort;
        this.userPort = userPort;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<QuestionDTO> getById(@PathVariable("id") Long id) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Getting question {} to the user {}", id, currentUserId);

        return ok(QuestionDTO.from(questionPort.find(id, currentUserId)));
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> get() {
        String currentUserId = userPort.getCurrentUserId();

        log.info("Getting all questions to the user {}", currentUserId);

        return ok(questionPort.findByUser(currentUserId)
                .stream()
                .map(QuestionDTO::from)
                .collect(toList()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated QuestionDTO question) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Saving question by user {}", currentUserId);

        question = question.toBuilder()
                .author(currentUserId)
                .build();
        questionPort.save(question);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/list")
    @ResponseStatus(HttpStatus.CREATED)
    public void createByList(@RequestBody @Validated List<QuestionDTO> questions) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Saving a list of {} questions to the user {}", questions.size(), currentUserId);

        questions.replaceAll(question -> question.toBuilder().author(currentUserId).build());
        questionPort.saveAll(new ArrayList<>(questions));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDTO> update(@RequestBody @Validated QuestionDTO question) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Updating question {} to the user {}", question.getId(), currentUserId);

        return ok(QuestionDTO.from(
                questionPort.update(question, currentUserId)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Deleting question {} to the user {}", id, currentUserId);
        questionPort.delete(id, currentUserId);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<QuestionDTO>> search(QuestionCriteriaDTO criteria) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String currentUserId = userPort.getCurrentUserId();
        log.info("Search by criteria {} to the user {}", mapper.writeValueAsString(criteria), currentUserId);

        return ok(questionPort.search(criteria.toQuestion(), currentUserId)
                .stream()
                .map(QuestionDTO::from)
                .collect(toList()));
    }

}
