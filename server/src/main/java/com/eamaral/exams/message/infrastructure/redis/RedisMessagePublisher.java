package com.eamaral.exams.message.infrastructure.redis;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.domain.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessagePublisher implements MessagePublisher {

    private final StringRedisTemplate template;

    private final ObjectMapper objectMapper;

    public RedisMessagePublisher(StringRedisTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Comment comment) {
        try {
            template.convertAndSend("question.comments",
                    objectMapper.writeValueAsString(comment)
            );
        } catch (JsonProcessingException e) {
            log.warn("Failed to publish question comment due to: {}", e.getMessage());
        }
    }
}
