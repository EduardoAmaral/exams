package com.eamaral.exams.message.application.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageDTO<T> {
    private final MessageType type;
    private final T data;

    public enum MessageType {
        FETCH_ALL_COMMENTS,
        NEW_COMMENT,
        DELETE_COMMENT
    }
}
