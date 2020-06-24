package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Comment;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerIntegrationTest {

    public static final String ENDPOINT = "/api/question/comment";

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Test
    public void create_shouldCallCommentServiceWithAComment() throws Exception {
        String userId = "123";
        ZonedDateTime now = ZonedDateTime.now();

        when(userPort.getCurrentUserId()).thenReturn(userId);
        when(commentPort.create(any())).thenReturn(CommentDTO.builder()
                .id(1L)
                .message("First Comment")
                .questionId(1L)
                .author("0987")
                .creationDate(now)
                .build());

        CommentDTO dto = CommentDTO.builder()
                .message("My First Comment")
                .questionId(1L)
                .build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.message", is("First Comment")))
                .andExpect(jsonPath("$.questionId", is(1)))
                .andExpect(jsonPath("$.author", is("0987")))
                .andExpect(jsonPath("$.creationDate", is(dateTimeFormatter.format(now))));

        verify(commentPort).create(commentArgumentCaptor.capture());
        Assertions.assertThat(commentArgumentCaptor.getValue())
                .extracting("author", "message", "questionId")
                .containsExactly(userId, "My First Comment", 1L);
    }

    @Test
    public void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
        QuestionDTO dto = QuestionDTO.builder().build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['questionId']", is("Question is required")))
                .andExpect(jsonPath("$.errors['message']", is("Message is required")));
    }

    @Test
    public void create_whenMessageIsGreaterThanExpected_shouldReturnBadRequest() throws Exception {
        CommentDTO dto = CommentDTO.builder()
                .message(new String(new char[301]).replace('\0', 'A'))
                .questionId(1L)
                .build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['message']", is("Comments cannot have more than 300 characters")));
    }
}