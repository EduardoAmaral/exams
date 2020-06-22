package com.eamaral.exams.message.infrastructure.redis.port;

import com.eamaral.exams.question.domain.Comment;

public interface MessagePublisher {
    void publish(Comment comment);
}
