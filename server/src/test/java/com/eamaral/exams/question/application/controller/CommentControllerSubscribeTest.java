package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.service.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerSubscribeTest {


    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController controller;

    @Test
    @DisplayName("should retrieve all comments from a question once subscribe")
    void subscribe_shouldRetrieveAllQuestionComments() {
        final ZonedDateTime now = ZonedDateTime.now();
        final List<Comment> comments = List.of(
                Comment.builder()
                        .id(1L)
                        .message("First Comment")
                        .questionId(1L)
                        .authorId("0987")
                        .creationDate(now)
                        .build(),
                Comment.builder()
                        .id(2L)
                        .message("Second Comment")
                        .questionId(1L)
                        .authorId("1234")
                        .creationDate(now)
                        .build()
        );
        when(commentService.findAllBy(1L)).thenReturn(comments);

        final MessageDTO<List<CommentDTO>> message = controller.subscribe(1L, () -> "1234");

        Assertions.assertThat(message.getType()).isEqualTo(MessageDTO.MessageType.FETCH_ALL_COMMENTS);
        Assertions.assertThat(message.getData()).extracting(CommentDTO::getMessage)
                .containsExactlyInAnyOrder("First Comment", "Second Comment");
    }
}