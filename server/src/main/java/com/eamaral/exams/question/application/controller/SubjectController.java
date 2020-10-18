package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/question/subject", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> list() {
        log.info("Getting all subjects");
        return ok(service.findAll()
                .stream()
                .map(SubjectDTO::from)
                .collect(toList()));
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> save(@RequestBody @Validated SubjectDTO subject) {
        log.info("Saving subject {}", subject.getDescription());
        return ok(SubjectDTO.from(service.save(subject.toDomain())));
    }
}
