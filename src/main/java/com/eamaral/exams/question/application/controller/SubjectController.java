package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.port.SubjectPort;
import io.swagger.annotations.Api;
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
@RequestMapping(value = "api/question/subject", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Subject")
public class SubjectController {

    private final SubjectPort subjectPort;

    public SubjectController(SubjectPort subjectPort) {
        this.subjectPort = subjectPort;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> list(){
        log.info("Getting all subjects");
        return ok(subjectPort.findAll()
                .stream()
                .map(SubjectDTO::from)
                .collect(toList()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Validated SubjectDTO subject){
        log.info("Saving subject {}", subject.getDescription());
        subjectPort.save(subject);
    }
}
