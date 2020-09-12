package com.eamaral.exams.message.infrastructure.redis;

import com.eamaral.exams.question.domain.Comment;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class RedisMessagePublisherTest {

    @Mock
    private StringRedisTemplate template;

    private RedisMessagePublisher publisher;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.findAndRegisterModules();

        publisher = new RedisMessagePublisher(template, mapper);
    }

    @Test
    @DisplayName("should publish a message on Redis")
    void publish_shouldSendAMessageToRedisChannel() {
        final ZonedDateTime creationDate = ZonedDateTime.now().withFixedOffsetZone();
        Comment comment = Comment.builder()
                .id(1L)
                .questionId(256L)
                .message("Monster")
                .authorId("512")
                .creationDate(creationDate)
                .build();

        publisher.publish(comment);

        verify(template).convertAndSend(eq("question.comments"), anyString());
    }
}