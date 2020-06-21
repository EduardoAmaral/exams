package com.eamaral.exams.message.application.redis;

import com.eamaral.exams.question.application.dto.CommentDTO;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
public class RedisMessageListener {

    private static final String QUESTION_COMMENTS = "/question/%s/comments";

    private final SimpMessagingTemplate messagingTemplate;

    public RedisMessageListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void onQuestionCommentMessage(String message) {
        CommentDTO comment = new GsonBuilder()
                .registerTypeAdapter(
                        ZonedDateTime.class,
                        (JsonDeserializer<ZonedDateTime>) (json, type, context) -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()))
                .create()
                .fromJson(message, CommentDTO.class);

        messagingTemplate.convertAndSend(
                String.format(QUESTION_COMMENTS, comment.getQuestionId()),
                comment);
    }
}
