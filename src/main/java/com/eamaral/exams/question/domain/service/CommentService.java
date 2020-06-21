package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentPort;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements CommentPort {

    private final CommentRepositoryPort repositoryPort;

    public CommentService(CommentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void create(Comment comment) {
        repositoryPort.create(comment);
    }

    @Override
    public List<Comment> findAllBy(long questionId) {
        return repositoryPort.findAllBy(questionId);
    }
}
