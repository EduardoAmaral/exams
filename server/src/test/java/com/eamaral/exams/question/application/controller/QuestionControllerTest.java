package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.AlternativeDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuestionControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/question";
    private final String currentUser = "1";
    private final Long questionId = 1L;

    @Captor
    private ArgumentCaptor<Question> questionCaptor;

    @Captor
    private ArgumentCaptor<List<Question>> questionListCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Test
    @DisplayName("should retrieve all questions")
    void get_shouldReturnAllQuestions() throws Exception {
        List<Question> questions = new ArrayList<>(getQuestionsDTO());

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(questionPort.findByUser(currentUser)).thenReturn(questions);

        mockMvc.perform(
                get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is("Question 1?")))
                .andExpect(jsonPath("$[0].solution", is("S1")))
                .andExpect(jsonPath("$[0].type", Matchers.is("True Or False")))
                .andExpect(jsonPath("$[0].shared", is(false)))
                .andExpect(jsonPath("$[0].correctAnswer", is("True")))
                .andExpect(jsonPath("$[0].topic", is("T01")))
                .andExpect(jsonPath("$[0].author", is(currentUser)))
                .andExpect(jsonPath("$[0].subject.description", is("English")))
                .andExpect(jsonPath("$[0].alternatives", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is("Question 2?")))
                .andExpect(jsonPath("$[1].solution", is("S2")))
                .andExpect(jsonPath("$[1].type", is("Multiple Choices")))
                .andExpect(jsonPath("$[1].shared", is(false)))
                .andExpect(jsonPath("$[1].correctAnswer", is("A")))
                .andExpect(jsonPath("$[1].topic", is("T02")))
                .andExpect(jsonPath("$[1].author", is(currentUser)))
                .andExpect(jsonPath("$[1].subject.description", is("English")))
                .andExpect(jsonPath("$[1].alternatives", hasSize(3)));
    }

    @Test
    @DisplayName("should retrieve a question by id")
    void getById_whenQuestionExists_shouldReturnAQuestion() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(questionPort.find(questionId, currentUser)).thenReturn(getTrueOrFalseQuestion());

        mockMvc.perform(
                get("/api/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.statement", is("Question 1?")))
                .andExpect(jsonPath("$.solution", is("S1")))
                .andExpect(jsonPath("$.type", Matchers.is("True Or False")))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.correctAnswer", is("True")))
                .andExpect(jsonPath("$.topic", is("T01")))
                .andExpect(jsonPath("$.author", is("1")))
                .andExpect(jsonPath("$.subject.description", is("English")))
                .andExpect(jsonPath("$.alternatives", hasSize(2)));
    }

    @Test
    @DisplayName("should return created status when creating a question")
    void create_whenAllFieldsAreValid_shouldReturnCreatedStatus() throws Exception {
        QuestionDTO dto = getTrueOrFalseQuestion();

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        doNothing().when(questionPort).save(questionCaptor.capture());

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        assertThat(questionCaptor.getValue()).extracting("author").isEqualTo(currentUser);
    }

    @Test
    @DisplayName("should return bad request when creating a question without required fields")
    void create_whenRequiredFieldsAreNotFilled_shouldReturnBadRequest() throws Exception {
        QuestionDTO dto = QuestionDTO.builder().build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['alternatives']", is("Alternatives are required")))
                .andExpect(jsonPath("$.errors['type']", is("Type is required")))
                .andExpect(jsonPath("$.errors['correctAnswer']", is("Correct answer is required")))
                .andExpect(jsonPath("$.errors['statement']", is("Statement is required")))
                .andExpect(jsonPath("$.errors['subject']", is("Subject is required")));
    }

    @Test
    void createByList_whenAllFieldsAreValid_shouldReturnQuestionsWithId() throws Exception {
        List<QuestionDTO> dtos = getQuestionsDTO();

        List<Question> questions = new ArrayList<>(dtos);

        String author = "40030";
        when(userPort.getCurrentUserId()).thenReturn(author);
        when(questionPort.saveAll(questionListCaptor.capture())).thenReturn(questions);

        mockMvc.perform(
                post("/api/question/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtos))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        assertThat(questionListCaptor.getValue())
                .withFailMessage("Expecting all elements to have user id %s", author)
                .allMatch(question -> question.getAuthor().equals(author));
    }

    @Test
    @DisplayName("should return the updated question when updating a question")
    void update_whenAllFieldsAreValid_shouldReturnAQuestionUpdated() throws Exception {
        QuestionDTO dto = getTrueOrFalseQuestion();

        Question question = dto.toBuilder()
                .solution("New Solution")
                .statement("New Statement")
                .shared(false)
                .correctAnswer("False")
                .build();

        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(questionPort.update(any(), eq(currentUser))).thenReturn(question);

        mockMvc.perform(
                put(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.statement", is("New Statement")))
                .andExpect(jsonPath("$.solution", is("New Solution")))
                .andExpect(jsonPath("$.type", is("True Or False")))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.correctAnswer", is("False")));
    }

    @Test
    @DisplayName("should return success when deleting a question")
    void delete_shouldReturnSuccess() throws Exception {
        String author = "590093";

        when(userPort.getCurrentUserId()).thenReturn(author);
        doNothing().when(questionPort).delete(eq(questionId), stringCaptor.capture());

        mockMvc.perform(
                delete("/api/question/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(stringCaptor.getValue()).isEqualTo(author);
    }

    @Test
    @DisplayName("search should use logged user")
    void search_shouldUseCurrentUserAsParameter() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(currentUser);
        when(questionPort.search(any(), stringCaptor.capture())).thenReturn(new ArrayList<>(getQuestionsDTO()));

        mockMvc.perform(
                get(ENDPOINT + "/search"))
                .andExpect(status().isOk());

        assertThat(stringCaptor.getValue())
                .isEqualTo(currentUser);
    }

    @ParameterizedTest
    @MethodSource("getSearchTerm")
    @DisplayName("search should retrieve questions by filter")
    void search_shouldReturnAListOfQuestionBySearchTerm(SearchQuestionScenario search) throws Exception {

        when(questionPort.search(questionCaptor.capture(), any())).thenReturn(new ArrayList<>(getQuestionsDTO()));

        mockMvc.perform(
                get(ENDPOINT + "/search")
                        .param(search.queryParam, search.searchTerm))
                .andExpect(status().isOk());

        assertThat(questionCaptor.getValue())
                .extracting(search.assertField)
                .isEqualTo(search.expectValue);
    }

    private static Stream<SearchQuestionScenario> getSearchTerm() {
        return Stream.of(
                SearchQuestionScenario.builder()
                        .queryParam("subject")
                        .searchTerm("1")
                        .assertField("subject.id")
                        .expectValue(1L)
                        .build(),
                SearchQuestionScenario.builder()
                        .queryParam("statement")
                        .searchTerm("Question")
                        .assertField("statement")
                        .expectValue("Question")
                        .build(),
                SearchQuestionScenario.builder()
                        .queryParam("topic")
                        .searchTerm("T01")
                        .assertField("topic")
                        .expectValue("T01")
                        .build(),
                SearchQuestionScenario.builder()
                        .queryParam("type")
                        .searchTerm(QuestionType.TRUE_OR_FALSE.toString())
                        .assertField("type")
                        .expectValue(QuestionType.TRUE_OR_FALSE)
                        .build());
    }

    @Builder
    @Getter
    private static class SearchQuestionScenario {
        private final String queryParam;
        private final String searchTerm;
        private final String assertField;
        private final Object expectValue;

        @Override
        public String toString() {
            return String.format("search should retrieve the questions where %s matches with %s",
                    queryParam,
                    searchTerm);
        }
    }

    private QuestionDTO getTrueOrFalseQuestion() {
        return QuestionDTO.builder()
                .id(questionId)
                .statement("Question 1?")
                .solution("S1")
                .type(QuestionType.TRUE_OR_FALSE)
                .shared(true)
                .correctAnswer("True")
                .topic("T01")
                .author(currentUser)
                .subject(SubjectDTO.builder()
                        .id(1L)
                        .description("English")
                        .build())
                .alternatives(
                        List.of(
                                AlternativeDTO.builder()
                                        .description("True")
                                        .build(),
                                AlternativeDTO.builder()
                                        .description("False")
                                        .build()))
                .build();
    }

    private List<QuestionDTO> getQuestionsDTO() {
        return List.of(
                getTrueOrFalseQuestion(),
                QuestionDTO.builder()
                        .id(2L)
                        .statement("Question 2?")
                        .solution("S2")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .shared(false)
                        .correctAnswer("A")
                        .topic("T02")
                        .author(currentUser)
                        .subject(SubjectDTO.builder()
                                .description("English")
                                .build())
                        .alternatives(getAlternatives())
                        .build());
    }

    private List<AlternativeDTO> getAlternatives() {
        return List.of(
                AlternativeDTO.builder()
                        .description("A")
                        .build(),
                AlternativeDTO.builder()
                        .description("B")
                        .build(),
                AlternativeDTO.builder()
                        .description("C")
                        .build()
        );
    }
}
