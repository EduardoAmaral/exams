package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentPort;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        List<Comment> comments = repositoryPort.findAllBy(questionId);

        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreationDate))
                .collect(toList());
    }
}
