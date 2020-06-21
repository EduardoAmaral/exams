package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Comment;

import java.util.List;

public interface CommentPort {
    void create(Comment comment);

    List<Comment> findAllBy(long questionId);
}
