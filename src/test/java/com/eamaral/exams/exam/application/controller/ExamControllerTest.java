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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/exam";

    @Captor
    private ArgumentCaptor<Exam> examCaptor;

    @Test
    public void save_shouldCreateAnExamAndReturnCreated() throws Exception {
        String title = "Exam";
        String currentUser = "90009";

        ExamDTO dto = ExamDTO.builder()
                .title(title)
                .questions(getQuestionsList())
                .build();

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        doNothing().when(examPort).save(examCaptor.capture());

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
        ).andExpect(status().isCreated());

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

    private List<QuestionDTO> getQuestionsList() {
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
