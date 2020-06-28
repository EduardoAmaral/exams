package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Comment;

import java.util.List;

public interface CommentRepositoryPort {
    Comment create(Comment comment);

    List<Comment> findAllBy(Long questionId);
}
