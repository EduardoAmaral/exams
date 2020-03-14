package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.AlternativeDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.Question;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest extends ControllerIntegrationTest {

    public static final String ENDPOINT = "/api/question";

    @Captor
    private ArgumentCaptor<Question> questionCaptor;

    @Captor
    private ArgumentCaptor<List<Question>> questionListCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Test
    public void get_shouldReturnAllQuestions() throws Exception {
        List<Question> questions = new ArrayList<>(getDtoList());

        String userId = "1";
        when(userPort.getCurrentUserId()).thenReturn(userId);
        when(questionPort.findByUser(userId)).thenReturn(questions);

        mockMvc.perform(
                get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is("Question 1?")))
                .andExpect(jsonPath("$[0].solution", is("S1")))
                .andExpect(jsonPath("$[0].type", Matchers.is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].sharable", is(false)))
                .andExpect(jsonPath("$[0].correctAnswer", is("True")))
                .andExpect(jsonPath("$[0].topic", is("T01")))
                .andExpect(jsonPath("$[0].userId", is(userId)))
                .andExpect(jsonPath("$[0].subject.description", is("English")))
                .andExpect(jsonPath("$[0].alternatives", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is("Question 2?")))
                .andExpect(jsonPath("$[1].solution", is("S2")))
                .andExpect(jsonPath("$[1].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].sharable", is(false)))
                .andExpect(jsonPath("$[1].correctAnswer", is("A")))
                .andExpect(jsonPath("$[1].topic", is("T02")))
                .andExpect(jsonPath("$[1].userId", is(userId)))
                .andExpect(jsonPath("$[1].subject.description", is("English")))
                .andExpect(jsonPath("$[1].alternatives", hasSize(3)));
    }

    @Test
    public void getById_whenQuestionExists_shouldReturnAQuestion() throws Exception {
        when(questionPort.find(1L)).thenReturn(getTrueOrFalseQuestion("S1", "Question 1?", true, "True"));

        mockMvc.perform(
                get("/api/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.statement", is("Question 1?")))
                .andExpect(jsonPath("$.solution", is("S1")))
                .andExpect(jsonPath("$.type", Matchers.is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.sharable", is(false)))
                .andExpect(jsonPath("$.correctAnswer", is("True")))
                .andExpect(jsonPath("$.topic", is("T01")))
                .andExpect(jsonPath("$.userId", is("1")))
                .andExpect(jsonPath("$.subject.description", is("English")))
                .andExpect(jsonPath("$.alternatives", hasSize(2)));
    }

    @Test
    public void create_whenAllFieldsAreValid_shouldReturnAQuestionWithId() throws Exception {
        String statement = "1 + 2 = ?";
        String solution = "3";
        String userId = "100";
        QuestionDTO dto = getTrueOrFalseQuestion(solution, statement, false, "A");

        Question question = dto.toBuilder().id(1L).build();

        when(userPort.getCurrentUserId()).thenReturn(userId);
        when(questionPort.save(questionCaptor.capture())).thenReturn(question);

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        assertThat(questionCaptor.getValue()).extracting("userId").isEqualTo(userId);
    }

    @Test
    public void create_withoutStatementAndType_shouldReturnBadRequest() throws Exception {
        QuestionDTO dto = QuestionDTO.builder().build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Question's alternatives are required",
                        "Question's type is required",
                        "Question's correct answer is required",
                        "Question's statement is required",
                        "Question's subject is required")));
    }

    @Test
    public void createByList_whenAllFieldsAreValid_shouldReturnQuestionsWithId() throws Exception {
        List<QuestionDTO> dtos = getDtoList();

        List<Question> questions = new ArrayList<>(dtos);

        String userId = "40030";
        when(userPort.getCurrentUserId()).thenReturn(userId);
        when(questionPort.saveAll(questionListCaptor.capture())).thenReturn(questions);

        mockMvc.perform(
                post("/api/question/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtos))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        assertThat(questionListCaptor.getValue())
                .withFailMessage("Expecting all elements to have user id %s", userId)
                .allMatch(question -> question.getUserId().equals(userId));
    }

    @Test
    public void update_whenAllFieldsAreValid_shouldReturnAQuestionUpdated() throws Exception {
        QuestionDTO dto = getTrueOrFalseQuestion("Solution", "Statement", true, "True");

        Question question = dto.toBuilder()
                .solution("New Solution")
                .statement("New Statement")
                .sharable(false)
                .correctAnswer("False")
                .build();

        when(questionPort.update(any())).thenReturn(question);

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
                .andExpect(jsonPath("$.type", is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$.sharable", is(false)))
                .andExpect(jsonPath("$.correctAnswer", is("False")));
    }

    @Test
    public void delete_shouldReturnSuccess() throws Exception {
        String userId = "590093";

        when(userPort.getCurrentUserId()).thenReturn(userId);
        doNothing().when(questionPort).delete(eq(1L), stringCaptor.capture());

        mockMvc.perform(
                delete("/api/question/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(stringCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    public void searchByStatement_shouldReturnAListOfQuestionByStatement() throws Exception {
        String statementCriteria = "Question";

        when(questionPort.search(questionCaptor.capture())).thenReturn(new ArrayList<>(getDtoList()));

        mockMvc.perform(
                get(ENDPOINT + "/search")
                        .param("statement", statementCriteria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statement", is("Question 1?")));

        assertThat(questionCaptor.getValue())
                .extracting("statement")
                .isEqualTo(statementCriteria);
    }

    @Test
    public void searchByType_shouldReturnAListOfQuestionByType() throws Exception {
        QuestionType typeCriteria = QuestionType.TRUE_OR_FALSE;

        when(questionPort.search(questionCaptor.capture())).thenReturn(new ArrayList<>(getDtoList()));

        mockMvc.perform(
                get(ENDPOINT + "/search")
                        .param("type", typeCriteria.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type", is(QuestionType.TRUE_OR_FALSE.toString())));

        assertThat(questionCaptor.getValue())
                .extracting("type")
                .isEqualTo(typeCriteria);
    }

    @Test
    public void searchByTopic_shouldReturnAListOfQuestionByTopic() throws Exception {
        String topicCriteria = "T01";

        when(questionPort.search(questionCaptor.capture())).thenReturn(new ArrayList<>(getDtoList()));

        mockMvc.perform(
                get(ENDPOINT + "/search")
                        .param("topic", topicCriteria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].topic", is(topicCriteria)));

        assertThat(questionCaptor.getValue())
                .extracting("topic")
                .isEqualTo(topicCriteria);
    }

    @Test
    public void searchBySubject_shouldReturnAListOfQuestionBySubject() throws Exception {
        Long subjectCriteria = 1L;

        when(questionPort.search(questionCaptor.capture())).thenReturn(new ArrayList<>(getDtoList()));

        mockMvc.perform(
                get(ENDPOINT + "/search")
                        .param("subject", subjectCriteria.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject.id", is(subjectCriteria.intValue())));

        assertThat(questionCaptor.getValue())
                .extracting("subject.id")
                .isEqualTo(subjectCriteria);
    }

    private QuestionDTO getTrueOrFalseQuestion(String solution, String statement, boolean sharable, String correctAnswer) {
        return QuestionDTO.builder()
                .id(1L)
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .active(true)
                .sharable(sharable)
                .correctAnswer(correctAnswer)
                .topic("T01")
                .userId("1")
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

    private List<QuestionDTO> getDtoList() {
        return List.of(
                getTrueOrFalseQuestion("S1", "Question 1?", true, "True"),
                QuestionDTO.builder()
                        .id(2L)
                        .statement("Question 2?")
                        .solution("S2")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .active(true)
                        .sharable(false)
                        .correctAnswer("A")
                        .topic("T02")
                        .userId("1")
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
