package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.*;

@Service
public class CommentService {

    private final CommentRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    private final MessagePublisher publisher;

    public CommentService(CommentRepositoryPort repositoryPort,
                          UserRepositoryPort userRepositoryPort,
                          MessagePublisher publisher) {
        this.repositoryPort = repositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.publisher = publisher;
    }

    public Comment create(Comment comment, String authorId) {
        final CompletableFuture<Comment> commentSupply = supplyAsync(
                () -> repositoryPort.create(comment.toBuilder()
                        .authorId(authorId)
                        .build()));

        final CompletableFuture<User> userSupply = supplyAsync(() -> userRepositoryPort.findById(authorId));

        final Comment newComment = commentSupply.join()
                .toBuilder()
                .authorName(userSupply.join().getFullName())
                .build();

        CompletableFuture.runAsync(() -> publisher.publish(newComment));

        return newComment;
    }

    public List<Comment> findAllBy(long questionId) {
        List<Comment> comments = repositoryPort.findAllBy(questionId);

        final Map<String, User> userById = userRepositoryPort.findAllByIds(comments.stream()
                .map(Comment::getAuthorId).collect(toSet()))
                .stream()
                .collect(toMap(User::getId, user -> user));

        return comments.stream()
                .map(comment -> comment.toBuilder()
                        .authorName(userById.get(comment.getAuthorId()).getFullName())
                        .build())
                .sorted(Comparator.comparing(Comment::getCreationDate).reversed())
                .collect(toList());
    }
}
