package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService service;

    @Mock
    private CommentRepositoryPort repository;

    @Test
    public void create_shouldCallRepositoryPortToSaveAComment() {
        Comment comment = CommentDTO.builder()
                .build();

        service.create(comment);

        verify(repository).create(comment);
    }

    @Test
    public void findAllBy_shouldCallRepositoryToRetrieveAllQuestionComments() {
        long questionId = 1L;

        service.findAllBy(questionId);

        verify(repository).findAllBy(questionId);
    }
}