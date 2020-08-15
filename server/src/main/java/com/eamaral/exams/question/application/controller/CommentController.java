package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.port.CommentPort;
import com.eamaral.exams.user.domain.port.UserPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.eamaral.exams.message.application.redis.dto.MessageDTO.MessageType.FETCH_ALL_COMMENTS;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/question/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final UserPort userPort;
    private final CommentPort commentPort;

    public CommentController(UserPort userPort, CommentPort commentPort) {
        this.userPort = userPort;
        this.commentPort = commentPort;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody @Validated CommentDTO comment) {
        String currentUserId = userPort.getCurrentUserId();
        log.info("Creating a comment on Question {} by user {}", comment.getQuestionId(), currentUserId);

        return ok(CommentDTO.from(
                commentPort.create(comment.toBuilder()
                        .author(currentUserId)
                        .build()))
        );
    }

    @SubscribeMapping("/question/{id}/comments")
    public MessageDTO<List<CommentDTO>> subscribe(@DestinationVariable Long id, Principal principal) {
        log.info("Subscribing {} in question {} comments channel", principal.getName(), id);

        return new MessageDTO<>(
                FETCH_ALL_COMMENTS,
                CommentDTO.from(commentPort.findAllBy(id))
        );
    }
}
