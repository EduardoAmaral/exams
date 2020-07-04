package com.eamaral.exams.message.infrastructure.redis;

import com.eamaral.exams.question.application.dto.CommentDTO;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class RedisMessagePublisherTest {

    @Mock
    private StringRedisTemplate template;

    @InjectMocks
    private RedisMessagePublisher publisher;

    @Test
    @DisplayName("should publish a message on Redis")
    void publish_shouldSendAMessageToRedisChannel() {
        CommentDTO comment = CommentDTO.builder()
                .id(1L)
                .questionId(256L)
                .message("Monster")
                .author("512")
                .creationDate(ZonedDateTime.now())
                .build();

        publisher.publish(comment);

        verify(template).convertAndSend("question.comments", new Gson().toJson(comment));
    }
}