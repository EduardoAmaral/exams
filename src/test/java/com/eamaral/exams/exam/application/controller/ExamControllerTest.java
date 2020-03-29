package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.application.dto.ExamTemplateDTO;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamControllerTest extends ControllerIntegrationTest {


    private static final String ENDPOINT = "/api/exam";

    @Test
    public void create_shouldProvideAnExamBasedOnATemplate_AndReturnCreatedStatus() throws Exception {
        String currentUserId = "100023";
        ExamDTO dto = ExamDTO.builder()
                .template(getExamTemplate())
                .build();

        when(userPort.getCurrentUserId()).thenReturn(currentUserId);

        mockMvc.perform(
                post(ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(userPort).getCurrentUserId();
        verify(examPort).create(any(), eq(currentUserId));
    }

    @Test
    public void create_whenTemplateIsNotProvided_shouldReturnBadRequest() throws Exception {
        ExamDTO dto = ExamDTO.builder().build();

        mockMvc.perform(
                post(ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder("Exam's template is required")));

        verify(examPort, never()).create(any(), anyString());
    }

    private ExamTemplateDTO getExamTemplate() {
        return ExamTemplateDTO.builder()
                .id("1")
                .title("Exam 1")
                .author("10001")
                .questions(getQuestions())
                .build();
    }

    private List<QuestionDTO> getQuestions() {
        return List.of(
                QuestionDTO.builder()
                        .id("1")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .build(),
                QuestionDTO.builder()
                        .id("2")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .build()
        );
    }
}