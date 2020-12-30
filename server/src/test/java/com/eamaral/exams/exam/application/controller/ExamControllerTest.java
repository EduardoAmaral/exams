package com.eamaral.exams.exam.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
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
    private final ZonedDateTime startDateTime = ZonedDateTime.now();
    private final ZonedDateTime endDateTime = ZonedDateTime.now().plusHours(2);
    private final String startDateTimeFormatted = startDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final String endDateTimeFormatted = endDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    private final String title = "Title 1";

    @Captor
    private ArgumentCaptor<Exam> examCaptor;

    @Test
    @DisplayName("should return created when creating an exam")
    void create_whenFieldsAreValid_AndReturnCreatedStatus() throws Exception {
        ExamDTO dto = getExamDTO();

        when(userService.getCurrentUserId()).thenReturn(currentUserId);

        mockMvc.perform(
                post(ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(userService).getCurrentUserId();
        verify(examService).create(examCaptor.capture());

        Exam exam = examCaptor.getValue();

        assertThat(exam.getTitle())
                .isEqualTo(title);

        assertThat(exam.getQuestions())
                .extracting("id")
                .matches(Objects::nonNull);

        assertThat(exam.getAuthorId())
                .isEqualTo(currentUserId);
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

        verify(examService, never()).create(any());
    }

    @Test
    @DisplayName("should retrieve all exams by their author")
    void get_shouldReturnAllExamsCreatedByTheUser() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(currentUserId);
        when(examService.findByUser(currentUserId)).thenReturn(
                List.of(getExamDTO(),
                        ExamDTO.builder()
                                .id(2L)
                                .title("Some New Title")
                                .authorId(currentUserId)
                                .mockTest(true)
                                .questions(getQuestions())
                                .build()
                )
        );

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1)].startDateTime", containsInAnyOrder(startDateTimeFormatted)))
                .andExpect(jsonPath("$[?(@.id == 1)].endDateTime", containsInAnyOrder(endDateTimeFormatted)))
                .andExpect(jsonPath("$[?(@.id == 1)].title", containsInAnyOrder(title)))
                .andExpect(jsonPath("$[?(@.id == 1)].authorId", containsInAnyOrder(currentUserId)))
                .andExpect(jsonPath("$[?(@.id == 1)].mockTest", contains(false)))
                .andExpect(jsonPath("$[?(@.id == 1)].questions[?(@.id == 1)].type", containsInAnyOrder("True Or False")))
                .andExpect(jsonPath("$[?(@.id == 1)].questions[?(@.id == 2)].type", containsInAnyOrder("Multiple Choices")))
                .andExpect(jsonPath("$[?(@.id == 2)].title", containsInAnyOrder("Some New Title")))
                .andExpect(jsonPath("$[?(@.id == 2)].authorId", containsInAnyOrder(currentUserId)))
                .andExpect(jsonPath("$[?(@.id == 2)].mockTest", contains(true)))
                .andExpect(jsonPath("$[?(@.id == 2)].questions[?(@.id == 1)].type", containsInAnyOrder("True Or False")))
                .andExpect(jsonPath("$[?(@.id == 2)].questions[?(@.id == 2)].type", containsInAnyOrder("Multiple Choices")));

        verify(userService).getCurrentUserId();
        verify(examService).findByUser(currentUserId);
    }

    @Test
    @DisplayName("should retrieve all available exams at current time")
    void getAvailable_shouldReturnAllExamsAvailableAtTheCurrentTime() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(currentUserId);
        when(examService.findAvailable()).thenReturn(List.of(getExamDTO()));

        mockMvc.perform(get(ENDPOINT + "/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].startDateTime", containsString(startDateTimeFormatted)))
                .andExpect(jsonPath("$[0].endDateTime", containsString(endDateTimeFormatted)))
                .andExpect(jsonPath("$[0].title", is(title)))
                .andExpect(jsonPath("$[0].authorId", is(currentUserId)))
                .andExpect(jsonPath("$[0].questions", hasSize(0)));

        verify(userService).getCurrentUserId();
        verify(examService).findAvailable();
    }

    @Test
    @DisplayName("should retrieve an exam by id")
    void getById_shouldReturnAnExistentExam() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(currentUserId);
        when(examService.findById(examId, currentUserId)).thenReturn(getExamDTO());

        mockMvc.perform(get(String.format("%s/%s", ENDPOINT, examId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.authorId", is(currentUserId)))
                .andExpect(jsonPath("$.startDateTime", containsString(startDateTimeFormatted)))
                .andExpect(jsonPath("$.endDateTime", containsString(endDateTimeFormatted)))
                .andExpect(jsonPath("$.questions", hasSize(2)));
    }

    @Test
    void delete_shouldReturnNoContentStatus() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(currentUserId);

        mockMvc.perform(
                delete(ENDPOINT + "/" + examId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userService).getCurrentUserId();
        verify(examService).delete(examId, currentUserId);
    }

    private ExamDTO getExamDTO() {
        return ExamDTO.builder()
                .id(1L)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .authorId(currentUserId)
                .title(title)
                .questions(getQuestions())
                .build();
    }

    private List<QuestionDTO> getQuestions() {
        final SubjectDTO english = SubjectDTO.builder()
                .description("English")
                .build();

        return List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .type(QuestionType.TRUE_OR_FALSE)
                        .subject(english)
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .subject(english)
                        .build()
        );
    }
}