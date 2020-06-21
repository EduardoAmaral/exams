package com.eamaral.exams.messages.application.redis;

import com.eamaral.exams.question.application.dto.CommentDTO;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class RedisMessagePublisherTest {

    @Mock
    private StringRedisTemplate template;

    @InjectMocks
    private RedisMessagePublisher publisher;

    @Test
    public void publish_shouldSendAMessageToRedisChannel() {
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