package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Comment;

public interface CommentRepositoryPort {
    Comment create(Comment comment);
}
