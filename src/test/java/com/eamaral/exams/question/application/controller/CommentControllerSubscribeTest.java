package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentPort;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerSubscribeTest {


    @Mock
    private CommentPort commentPort;

    @InjectMocks
    private CommentController controller;

    @Test
    public void subscribe_shouldRetrieveAllQuestionComments() {
        final ZonedDateTime now = ZonedDateTime.now();
        final List<Comment> comments = List.of(
                CommentDTO.builder()
                        .id(1L)
                        .message("First Comment")
                        .questionId(1L)
                        .author("0987")
                        .creationDate(now)
                        .build(),
                CommentDTO.builder()
                        .id(2L)
                        .message("Second Comment")
                        .questionId(1L)
                        .author("1234")
                        .creationDate(now)
                        .build()
        );
        when(commentPort.findAllBy(1L)).thenReturn(comments);

        final MessageDTO<List<CommentDTO>> message = controller.subscribe(1L, () -> "1234");

        Assertions.assertThat(message.getType()).isEqualTo(MessageDTO.MessageType.FETCH_ALL_COMMENTS);
        Assertions.assertThat(message.getData()).isEqualTo(comments);
    }
}