package com.eamaral.exams.message.application.redis;

import com.eamaral.exams.question.domain.Comment;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessagePublisher {

    private final StringRedisTemplate template;

    public RedisMessagePublisher(StringRedisTemplate template) {
        this.template = template;
    }

    public void publish(Comment comment) {
        template.convertAndSend("question.comments", new Gson().toJson(comment));
    }
}
