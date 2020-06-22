package com.eamaral.exams.message.infrastructure.redis;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.domain.Comment;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessagePublisher implements MessagePublisher {

    private final StringRedisTemplate template;

    public RedisMessagePublisher(StringRedisTemplate template) {
        this.template = template;
    }

    public void publish(Comment comment) {
        template.convertAndSend("question.comments", new Gson().toJson(comment));
    }
}
