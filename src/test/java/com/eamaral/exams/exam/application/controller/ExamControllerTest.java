package com.eamaral.exams.exam.application.controller;


import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.Exam;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/exam";

    @Captor
    private ArgumentCaptor<Exam> examCaptor;

    @Test
    public void create_shouldCreateAnExamAndReturnCreated() throws Exception {
        String title = "Exam";
        String currentUser = "90009";

        ExamDTO dto = ExamDTO.builder()
                .title(title)
                .questions(getQuestions())
                .build();

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        doNothing().when(examPort).save(examCaptor.capture());

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(examPort).save(any());

        Exam examSaved = examCaptor.getValue();

        assertThat(examSaved.getTitle())
                .isEqualTo(title);

        assertThat(examSaved.getQuestions())
                .extracting("id")
                .matches(Objects::nonNull);

        assertThat(examSaved.getAuthor())
                .isEqualTo(currentUser);
    }

    @Test
    public void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
        ExamDTO dto = ExamDTO.builder()
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
    public void get_shouldReturnAllExamsCreatedByTheCurrentUser() throws Exception {
        String currentUser = "10001";

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(examPort.findByUser(currentUser)).thenReturn(getExams());

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Exam 1")))
                .andExpect(jsonPath("$[0].author", is(currentUser)))
                .andExpect(jsonPath("$[0].questions", hasSize(2)))
                .andExpect(jsonPath("$[1].title", is("Exam 2")))
                .andExpect(jsonPath("$[1].author", is(currentUser)))
                .andExpect(jsonPath("$[1].questions", hasSize(2)));
    }

    private List<Exam> getExams() {
        return List.of(
                ExamDTO.builder()
                        .title("Exam 1")
                        .author("10001")
                        .questions(getQuestions())
                        .build(),
                ExamDTO.builder()
                        .title("Exam 2")
                        .author("10001")
                        .questions(getQuestions())
                        .build());
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
