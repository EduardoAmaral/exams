package com.eamaral.exams.message.application.redis;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.eamaral.exams.message.application.redis.dto.MessageDTO.MessageType.NEW_COMMENT;

@Slf4j
@Component
public class RedisMessageListener {

    private static final String QUESTION_COMMENTS = "/question/%s/comments";

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;

    public RedisMessageListener(SimpMessagingTemplate messagingTemplate,
                                ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    public void onQuestionCommentMessage(String message) throws JsonProcessingException {
        log.info("Receiving question comment message: {}", message);

        CommentDTO comment = objectMapper.readValue(message, new TypeReference<>() {
        });

        messagingTemplate.convertAndSend(
                String.format(QUESTION_COMMENTS, comment.getQuestionId()),
                new MessageDTO<>(NEW_COMMENT, comment));
    }
}
