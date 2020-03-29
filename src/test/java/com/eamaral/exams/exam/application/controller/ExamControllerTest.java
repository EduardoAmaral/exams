package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.application.dto.ExamTemplateDTO;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
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

    private final String currentUserId = "100023";

    private final String templateTitle = "Exam 1";

    private final LocalDateTime startDateTime = LocalDateTime.now();

    private final LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);

    @Test
    public void create_shouldProvideAnExamBasedOnATemplate_AndReturnCreatedStatus() throws Exception {
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

    @Test
    public void get_shouldReturnAllExamsCreatedByTheUser() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUserId);
        when(examPort.findByUser(currentUserId)).thenReturn(singletonList(getExam()));

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].startDateTime", is(startDateTime.toString())))
                .andExpect(jsonPath("$[0].endDateTime", is(endDateTime.toString())))
                .andExpect(jsonPath("$[0].mockTest", is(false)))
                .andExpect(jsonPath("$[0].template.title", is(templateTitle)))
                .andExpect(jsonPath("$[0].template.author", is(currentUserId)))
                .andExpect(jsonPath("$[0].template.questions", hasSize(2)));

        verify(userPort).getCurrentUserId();
        verify(examPort).findByUser(currentUserId);
    }

    @Test
    public void getAvailable_shouldReturnAllExamsAvailableAtTheCurrentTime() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUserId);
        when(examPort.findAvailable()).thenReturn(singletonList(getExam()));

        mockMvc.perform(get(ENDPOINT + "/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].startDateTime", is(startDateTime.toString())))
                .andExpect(jsonPath("$[0].endDateTime", is(endDateTime.toString())))
                .andExpect(jsonPath("$[0].mockTest", is(false)))
                .andExpect(jsonPath("$[0].template.title", is(templateTitle)))
                .andExpect(jsonPath("$[0].template.author", is(currentUserId)))
                .andExpect(jsonPath("$[0].template.questions", hasSize(2)));

        verify(userPort).getCurrentUserId();
        verify(examPort).findAvailable();
    }

    private ExamDTO getExam() {
        return ExamDTO.builder()
                .id("1")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .mockTest(false)
                .template(getExamTemplate())
                .build();
    }


    private ExamTemplateDTO getExamTemplate() {
        return ExamTemplateDTO.builder()
                .id("1")
                .title(templateTitle)
                .author(currentUserId)
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