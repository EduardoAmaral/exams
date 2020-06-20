package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Comment;

public interface CommentPort {
    void create(Comment comment);
}
