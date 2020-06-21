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

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerIntegrationTest {

    public static final String ENDPOINT = "/api/question/comment";

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    @Test
    public void create_shouldCallCommentServiceWithAComment() throws Exception {
        String userId = "123";
        when(userPort.getCurrentUserId()).thenReturn(userId);

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
                .andExpect(MockMvcResultMatchers.status().isCreated());

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
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Comment's question is required",
                        "Comment's message is required")));
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
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Comments cannot have more than 300 characters")));
    }

    @Test
    public void getAllByQuestion_shouldRetrieveAllQuestionComments() throws Exception {
        when(commentPort.findAllBy(1L)).thenReturn(
                List.of(
                        CommentDTO.builder()
                                .id(1L)
                                .message("First Comment")
                                .questionId(1L)
                                .author("0987")
                                .build(),
                        CommentDTO.builder()
                                .id(2L)
                                .message("Second Comment")
                                .questionId(1L)
                                .author("1234")
                                .build()
                )
        );

        mockMvc.perform(
                get(ENDPOINT).param("questionId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].message", is("First Comment")))
                .andExpect(jsonPath("$[0].questionId", is(1)))
                .andExpect(jsonPath("$[0].author", is("0987")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].message", is("Second Comment")))
                .andExpect(jsonPath("$[1].questionId", is(1)))
                .andExpect(jsonPath("$[1].author", is("1234")));

        verify(commentPort).findAllBy(1L);
    }
}