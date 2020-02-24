package com.eamaral.exams.question.application.controllers;

import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.services.port.QuestionPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "question", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {

    private final QuestionPort questionPort;

    @Autowired
    public QuestionController(QuestionPort questionPort) {
        this.questionPort = questionPort;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<QuestionDTO> getById(@PathVariable("id") long id){
        QuestionDTO question = QuestionDTO.from(questionPort.findById(id));

        return ok(question);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> get(){
        return ok(questionPort.findAll()
                .stream()
                .map(QuestionDTO::from)
                .collect(toList()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated QuestionDTO question) {
        questionPort.save(question);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/list")
    public ResponseEntity<List<QuestionDTO>> createByList(@RequestBody @Validated List<QuestionDTO> questions) {
        List<QuestionDTO> result = questionPort
                .saveAll(new ArrayList<>(questions))
                .stream()
                .map(QuestionDTO::from)
                .collect(toList());
        return ok(result);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDTO> update(@RequestBody @Validated QuestionDTO question) {
        return ok(QuestionDTO.from(
                questionPort.update(question)));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        questionPort.delete(id);
    }

}
