package com.eamaral.exams.message.application.redis;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

import static com.eamaral.exams.message.application.redis.dto.MessageDTO.MessageType.NEW_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class RedisMessageListenerTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private RedisMessageListener listener;

    @Captor
    private ArgumentCaptor<String> channelArgumentCaptor;

    @Captor
    private ArgumentCaptor<MessageDTO<CommentDTO>> commentArgumentCaptor;

    @Test
    public void onQuestionCommentMessage_shouldSendACommentToTheTopic() {
        listener.onQuestionCommentMessage("{ " +
                "\"id\": 1, " +
                "\"questionId\": 12256, " +
                "\"message\": \"Comment\", " +
                "\"creationDate\": \"2020-06-21T00:27:50.000500-03:00\", " +
                "\"author\": \"1234\" " +
                "}");

        verify(simpMessagingTemplate).convertAndSend(
                channelArgumentCaptor.capture(),
                commentArgumentCaptor.capture()
        );

        assertThat(channelArgumentCaptor.getValue()).isEqualTo("/question/12256/comments");

        MessageDTO<CommentDTO> message = commentArgumentCaptor.getValue();
        assertThat(message.getType()).isEqualTo(NEW_COMMENT);

        CommentDTO result = message.getData();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuestionId()).isEqualTo(12256L);
        assertThat(result.getCreationDate()).isEqualTo(ZonedDateTime.parse("2020-06-21T00:27:50.000500-03:00"));
        assertThat(result.getMessage()).isEqualTo("Comment");
        assertThat(result.getAuthor()).isEqualTo("1234");
    }
}