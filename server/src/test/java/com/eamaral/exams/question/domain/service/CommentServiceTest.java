package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService service;

    @Mock
    private CommentRepositoryPort repository;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private MessagePublisher publisher;

    @Test
    @DisplayName("should create a comment")
    void create_shouldCallRepositoryPortToSaveAComment() {
        final Comment comment = Comment.builder()
                .authorId("1")
                .build();

        final Comment newComment = Comment.builder()
                .id(1L).authorId("1").build();

        when(repository.create(comment)).thenReturn(newComment);

        service.create(comment, "1");

        verify(repository).create(comment);
    }

    @Test
    @DisplayName("should publish a message after create a comment")
    void create_shouldPublishAMessage() {
        Comment comment = Comment.builder()
                .build();

        final Comment newComment = Comment.builder().id(1L).build();

        CompletableFuture.runAsync(
                () -> service.create(comment, "1")).thenApply(s -> {
            verify(publisher).publish(newComment);
            return s;
        });
    }

    @Test
    @DisplayName("should retrieve all comments from a question ordered by the newest")
    void findAllByQuestionId_Order() {
        long questionId = 1L;
        when(repository.findAllBy(questionId)).thenReturn(List.of(
                Comment.builder()
                        .authorId("1")
                        .message("Today Comment")
                        .creationDate(ZonedDateTime.now())
                        .build(),
                Comment.builder()
                        .authorId("1")
                        .message("Yesterday Comment")
                        .creationDate(ZonedDateTime.now().minusDays(1L))
                        .build()));

        when(userRepositoryPort.findAllByIds(Set.of("1")))
                .thenReturn(List.of(
                        User.builder()
                                .id("1")
                                .name("Min-Young")
                                .surname("Park")
                                .build()));

        List<Comment> comments = service.findAllBy(questionId);

        verify(repository).findAllBy(questionId);

        assertThat(comments)
                .extracting("message")
                .containsExactly("Today Comment", "Yesterday Comment");
    }

    @Test
    @DisplayName("should retrieve all comments from a question with their respective author names")
    void findAllBy_author() {
        long questionId = 1L;
        when(repository.findAllBy(questionId)).thenReturn(List.of(
                Comment.builder()
                        .message("Today Comment")
                        .authorId("1")
                        .creationDate(ZonedDateTime.now())
                        .build(),
                Comment.builder()
                        .authorId("2")
                        .message("Yesterday Comment")
                        .creationDate(ZonedDateTime.now().minusDays(1L))
                        .build()
        ));

        when(userRepositoryPort.findAllByIds(Set.of("1", "2")))
                .thenReturn(List.of(
                        User.builder()
                                .id("1")
                                .name("Min-Young")
                                .surname("Park")
                                .build(),
                        User.builder()
                                .id("2")
                                .name("Jong-Suk")
                                .surname("Lee")
                                .build())
                );

        List<Comment> comments = service.findAllBy(questionId);

        verify(repository).findAllBy(questionId);

        assertThat(comments)
                .extracting(Comment::getMessage, Comment::getAuthorId, Comment::getAuthorName)
                .containsExactlyInAnyOrder(
                        tuple("Today Comment", "1", "Min-Young Park"),
                        tuple("Yesterday Comment", "2", "Jong-Suk Lee")
                );
    }
}