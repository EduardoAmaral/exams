package com.amaral.exams.question.application.controllers;

import com.amaral.exams.question.application.dto.QuestionDTO;
import com.amaral.exams.question.domain.services.port.QuestionPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "question", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {

    @Autowired
    private QuestionPort questionPort;

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
    public ResponseEntity<QuestionDTO> create(@RequestBody @Validated QuestionDTO question) {
        return ok(QuestionDTO.from(
                questionPort.save(question.toDomain())));
    }
}
