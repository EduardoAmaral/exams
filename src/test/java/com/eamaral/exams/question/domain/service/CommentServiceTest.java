package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.message.infrastructure.redis.port.MessagePublisher;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService service;

    @Mock
    private CommentRepositoryPort repository;

    @Mock
    private MessagePublisher publisher;

    @Test
    public void create_shouldCallRepositoryPortToSaveACommentAndPublishAMessage() {
        Comment comment = CommentDTO.builder()
                .build();

        final Comment newComment = CommentDTO.builder().id(1L).build();
        when(repository.create(comment)).thenReturn(newComment);

        service.create(comment);

        verify(repository).create(comment);
        verify(publisher).publish(newComment);
    }

    @Test
    public void findAllBy_shouldRetrieveAllQuestionCommentsOrderByTheNewest() {
        long questionId = 1L;
        when(repository.findAllBy(questionId)).thenReturn(List.of(
                CommentDTO.builder()
                        .message("Today Comment")
                        .creationDate(ZonedDateTime.now())
                        .build(),
                CommentDTO.builder()
                        .message("Yesterday Comment")
                        .creationDate(ZonedDateTime.now().minusDays(1L))
                        .build()
        ));

        List<Comment> comments = service.findAllBy(questionId);

        verify(repository).findAllBy(questionId);

        assertThat(comments)
                .extracting("message")
                .containsExactly("Today Comment", "Yesterday Comment");
    }
}