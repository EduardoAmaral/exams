package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/exam";
    private final String currentUserId = "100023";
    private final Long examId = 1L;
    private final LocalDateTime startDateTime = LocalDateTime.now();
    private final LocalDateTime endDateTime = LocalDateTime.now().plusHours(2);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final String title = "Title 1";

    @Captor
    private ArgumentCaptor<Exam> examCaptor;

    @Test
    @DisplayName("should return created when creating an exam")
    void create_whenFieldsAreValid_AndReturnCreatedStatus() throws Exception {
        ExamDTO dto = getExamDTO();

        when(userPort.getCurrentUserId()).thenReturn(currentUserId);

        mockMvc.perform(
                post(ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(userPort).getCurrentUserId();
        verify(examPort).create(examCaptor.capture(), eq(currentUserId));

        Exam exam = examCaptor.getValue();

        assertThat(exam.getTitle())
                .isEqualTo(title);

        assertThat(exam.getQuestions())
                .extracting("id")
                .matches(Objects::nonNull);

        assertThat(exam.getAuthor())
                .isEqualTo(currentUserId);

        assertThat(exam.isMockTest())
                .isFalse();
    }

    @Test
    @DisplayName("should return bad request when creating an exam without required fields")
    void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
        ExamDTO dto = ExamDTO.builder().build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['title']", is("Title is required")))
                .andExpect(jsonPath("$.errors['questions']", is("Questions are required")));

        verify(examPort, never()).create(any(), anyString());
    }

    @Test
    @DisplayName("should retrieve all exams by their author")
    void get_shouldReturnAllExamsCreatedByTheUser() throws Exception {

        when(userPort.getCurrentUserId()).thenReturn(currentUserId);
        when(examPort.findByUser(currentUserId)).thenReturn(singletonList(getExamDTO()));

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].startDateTime", is(dateTimeFormatter.format(startDateTime))))
                .andExpect(jsonPath("$[0].endDateTime", is(dateTimeFormatter.format(endDateTime))))
                .andExpect(jsonPath("$[0].mockTest", is(false)))
                .andExpect(jsonPath("$[0].title", is(title)))
                .andExpect(jsonPath("$[0].author", is(currentUserId)))
                .andExpect(jsonPath("$[0].questions", hasSize(2)));

        verify(userPort).getCurrentUserId();
        verify(examPort).findByUser(currentUserId);
    }

    @Test
    @DisplayName("should retrieve all available exams at current time")
    void getAvailable_shouldReturnAllExamsAvailableAtTheCurrentTime() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUserId);
        when(examPort.findAvailable()).thenReturn(singletonList(getExamDTO()));

        mockMvc.perform(get(ENDPOINT + "/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].startDateTime", is(dateTimeFormatter.format(startDateTime))))
                .andExpect(jsonPath("$[0].endDateTime", is(dateTimeFormatter.format(endDateTime))))
                .andExpect(jsonPath("$[0].mockTest", is(false)))
                .andExpect(jsonPath("$[0].title", is(title)))
                .andExpect(jsonPath("$[0].author", is(currentUserId)))
                .andExpect(jsonPath("$[0].questions", hasSize(0)));

        verify(userPort).getCurrentUserId();
        verify(examPort).findAvailable();
    }

    @Test
    @DisplayName("should retrieve an exam by id")
    void getById_shouldReturnAnExistentExam() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUserId);
        when(examPort.findById(examId, currentUserId)).thenReturn(getExamDTO());

        mockMvc.perform(get(String.format("%s/%s", ENDPOINT, examId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.author", is(currentUserId)))
                .andExpect(jsonPath("$.startDateTime", is(dateTimeFormatter.format(startDateTime))))
                .andExpect(jsonPath("$.endDateTime", is(dateTimeFormatter.format(endDateTime))))
                .andExpect(jsonPath("$.mockTest", is(false)))
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    void delete_shouldReturnNoContentStatus() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUserId);

        mockMvc.perform(
                delete(ENDPOINT + "/" + examId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userPort).getCurrentUserId();
        verify(examPort).delete(examId, currentUserId);
    }

    private ExamDTO getExamDTO() {
        return ExamDTO.builder()
                .id(1L)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .mockTest(false)
                .author(currentUserId)
                .title(title)
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