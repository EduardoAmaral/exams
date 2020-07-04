package com.eamaral.exams.message.application.redis;

import com.eamaral.exams.message.application.redis.dto.MessageDTO;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static com.eamaral.exams.message.application.redis.dto.MessageDTO.MessageType.NEW_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class RedisMessageListenerTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private RedisMessageListener listener;

    @Captor
    private ArgumentCaptor<String> channelArgumentCaptor;

    @Captor
    private ArgumentCaptor<MessageDTO<CommentDTO>> commentArgumentCaptor;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.findAndRegisterModules();

        listener = new RedisMessageListener(simpMessagingTemplate, mapper);
    }

    @Test
    @DisplayName("should send a question's comment to its topic when receiving a message")
    void onQuestionCommentMessage_shouldSendACommentToTheTopic() throws JsonProcessingException {
        final ZonedDateTime creationDate = ZonedDateTime.now().withFixedOffsetZone();
        listener.onQuestionCommentMessage("{ " +
                "\"id\": 1, " +
                "\"questionId\": 12256, " +
                "\"message\": \"Comment\", " +
                "\"creationDate\": \""+ creationDate +"\", " +
                "\"author\": \"1234\" " +
                "}");

        verify(simpMessagingTemplate).convertAndSend(
                channelArgumentCaptor.capture(),
                commentArgumentCaptor.capture()
        );

        ZonedDateTime.now().toEpochSecond();

        assertThat(channelArgumentCaptor.getValue()).isEqualTo("/question/12256/comments");

        MessageDTO<CommentDTO> message = commentArgumentCaptor.getValue();
        assertThat(message.getType()).isEqualTo(NEW_COMMENT);

        CommentDTO result = message.getData();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuestionId()).isEqualTo(12256L);
        assertThat(result.getCreationDate()).isEqualTo(creationDate);
        assertThat(result.getMessage()).isEqualTo("Comment");
        assertThat(result.getAuthor()).isEqualTo("1234");
    }
}