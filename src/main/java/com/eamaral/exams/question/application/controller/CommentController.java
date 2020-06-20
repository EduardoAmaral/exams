package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.port.CommentPort;
import com.eamaral.exams.user.domain.port.UserPort;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "api/question/comment", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Comment")
public class CommentController {

    private final UserPort userPort;
    private final CommentPort commentPort;

    public CommentController(UserPort userPort, CommentPort commentPort) {
        this.userPort = userPort;
        this.commentPort = commentPort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated CommentDTO comment) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Creating a comment on Question {} by user {}", comment.getQuestionId(), currentUserId);

        commentPort.create(comment.toBuilder()
                .author(currentUserId)
                .build());
    }
}