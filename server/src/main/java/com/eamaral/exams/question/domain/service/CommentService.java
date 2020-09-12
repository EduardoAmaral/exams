package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class CommentService {

    private final CommentRepositoryPort repositoryPort;

    private final MessagePublisher publisher;

    public CommentService(CommentRepositoryPort repositoryPort, MessagePublisher publisher) {
        this.repositoryPort = repositoryPort;
        this.publisher = publisher;
    }

    public Comment create(Comment comment) {
        Comment newComment = repositoryPort.create(comment);

        CompletableFuture.runAsync(() -> publisher.publish(newComment));

        return newComment;
    }

    public List<Comment> findAllBy(long questionId) {
        List<Comment> comments = repositoryPort.findAllBy(questionId);

        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreationDate).reversed())
                .collect(toList());
    }
}
