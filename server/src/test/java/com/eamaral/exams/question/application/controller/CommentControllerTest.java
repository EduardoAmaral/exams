package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.application.dto.CommentDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Comment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/question/comment";

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    @Test
    @DisplayName("should integrate with commentService when creating a comment")
    void create_shouldCallCommentServiceWithAComment() throws Exception {
        String userId = "123";
        ZonedDateTime now = ZonedDateTime.now().withFixedOffsetZone();

        when(userService.getCurrentUserId()).thenReturn(userId);
        when(commentService.create(any(), eq(userId))).thenReturn(Comment.builder()
                .id(1L)
                .message("First Comment")
                .questionId(1L)
                .authorId("0987")
                .authorName("Bo-young Park")
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
                .andExpect(jsonPath("$.authorId", is("0987")))
                .andExpect(jsonPath("$.authorName", is("Bo-young Park")))
                .andExpect(jsonPath("$.creationDate", containsString(now.toLocalDate().toString())));

        verify(commentService).create(commentArgumentCaptor.capture(), eq(userId));
        Assertions.assertThat(commentArgumentCaptor.getValue())
                .extracting("message", "questionId")
                .containsExactly("My First Comment", 1L);
    }

    @Test
    @DisplayName("should validate required fields when creating a comment")
    void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
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
    @DisplayName("should validate message length when creating a comment")
    void create_whenMessageIsGreaterThanExpected_shouldReturnBadRequest() throws Exception {
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
                .andExpect(jsonPath("$.errors['message']", is("Comments should have a maximum of 600 characters")));
    }
}