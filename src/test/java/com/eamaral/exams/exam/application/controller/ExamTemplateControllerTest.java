package com.eamaral.exams.exam.application.controller;


import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamTemplateDTO;
import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamTemplateControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/exam/template";

    @Captor
    private ArgumentCaptor<ExamTemplate> examCaptor;

    private final String currentUser = "10001";
    private final Long examId = 1L;

    @Test
    public void create_shouldCreateAnExamTemplateAndReturnCreated() throws Exception {
        String title = "Exam";

        ExamTemplateDTO dto = ExamTemplateDTO.builder()
                .title(title)
                .questions(getQuestions())
                .build();

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        doNothing().when(examTemplatePort).save(examCaptor.capture());

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(examTemplatePort).save(any());

        ExamTemplate examTemplateSaved = examCaptor.getValue();

        assertThat(examTemplateSaved.getTitle())
                .isEqualTo(title);

        assertThat(examTemplateSaved.getQuestions())
                .extracting("id")
                .matches(Objects::nonNull);

        assertThat(examTemplateSaved.getAuthor())
                .isEqualTo(currentUser);
    }

    @Test
    public void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
        ExamTemplateDTO dto = ExamTemplateDTO.builder()
                .questions(emptyList())
                .build();
        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Exam's title is required",
                        "Exam's questions are required")));
    }

    @Test
    public void get_shouldReturnAllExamTemplatesCreatedByTheCurrentUser() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(examTemplatePort.findByUser(currentUser)).thenReturn(getExamTemplates());

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Exam 1")))
                .andExpect(jsonPath("$[0].author", is(currentUser)))
                .andExpect(jsonPath("$[0].questions", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Exam 2")))
                .andExpect(jsonPath("$[1].author", is(currentUser)))
                .andExpect(jsonPath("$[1].questions", hasSize(2)));
    }

    @Test
    public void getById_shouldReturnAnExistentExamTemplate() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(examTemplatePort.findById(examId, currentUser)).thenReturn(getExamTemplate());

        mockMvc.perform(get(String.format("%s/%s", ENDPOINT, examId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Exam 1")))
                .andExpect(jsonPath("$.author", is(currentUser)))
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    public void delete_shouldReturnNoContentStatus() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUser);

        mockMvc.perform(
                delete(ENDPOINT + "/" + examId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userPort).getCurrentUserId();
        verify(examTemplatePort).delete(examId, currentUser);
    }

    private List<ExamTemplate> getExamTemplates() {
        return List.of(
                getExamTemplate(),
                ExamTemplateDTO.builder()
                        .id(2L)
                        .title("Exam 2")
                        .author("10001")
                        .questions(getQuestions())
                        .build());
    }

    private ExamTemplateDTO getExamTemplate() {
        return ExamTemplateDTO.builder()
                .id(1L)
                .title("Exam 1")
                .author("10001")
                .questions(getQuestions())
                .build();
    }

    private List<QuestionDTO> getQuestions() {
        return List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .type(QuestionType.TRUE_OR_FALSE)
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .build()
        );
    }

}
