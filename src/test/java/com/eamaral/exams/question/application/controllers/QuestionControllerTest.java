package com.eamaral.exams.question.application.controllers;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.AlternativeDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Question;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest extends ControllerIntegrationTest {

    @Test
    public void get_shouldReturnAllQuestions() throws Exception {
        List<Question> questions = new ArrayList<>(getDtoList());

        when(questionService.findAll()).thenReturn(questions);

        mockMvc.perform(
                get("/question"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is("Question 1?")))
                .andExpect(jsonPath("$[0].solution", is("S1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].sharable", is(false)))
                .andExpect(jsonPath("$[0].correctAnswer", is("True")))
                .andExpect(jsonPath("$[0].topic", is("T01")))
                .andExpect(jsonPath("$[0].alternatives", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is("Question 2?")))
                .andExpect(jsonPath("$[1].solution", is("S2")))
                .andExpect(jsonPath("$[1].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].sharable", is(false)))
                .andExpect(jsonPath("$[1].correctAnswer", is("A")))
                .andExpect(jsonPath("$[1].topic", is("T02")))
                .andExpect(jsonPath("$[1].alternatives", hasSize(3)));
    }

    @Test
    public void getById_whenQuestionExists_shouldReturnAQuestion() throws Exception {
        when(questionService.find(1L)).thenReturn(QuestionDTO.builder().statement("ABC").build());

        mockMvc.perform(
                get("/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statement", is("ABC")));
    }

    @Test
    public void create_whenAllFieldsAreValid_shouldReturnAQuestionWithId() throws Exception {
        String statement = "1 + 2 = ?";
        String solution = "3";
        QuestionDTO dto = QuestionDTO.builder()
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .active(true)
                .sharable(false)
                .correctAnswer("A")
                .alternatives(
                        List.of(
                                AlternativeDTO.builder()
                                        .description("True")
                                        .build(),
                                AlternativeDTO.builder()
                                        .description("False")
                                        .build()))
                .build();

        Question question = dto.toBuilder().id(1L).build();

        when(questionService.save(any())).thenReturn(question);

        mockMvc.perform(
                post("/question")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void create_withoutStatementAndType_shouldReturnBadRequest() throws Exception {
        QuestionDTO dto = QuestionDTO.builder()
                .solution("3")
                .active(true)
                .sharable(false)
                .build();

        mockMvc.perform(
                post("/question")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Question's alternatives are required",
                        "Question's type is required",
                        "Question's correct answer is required",
                        "Question's statement is required")));
    }

    @Test
    public void createByList_whenAllFieldsAreValid_shouldReturnQuestionsWithId() throws Exception {
        List<QuestionDTO> dtos = getDtoList();

        List<Question> questions = new ArrayList<>(dtos);

        when(questionService.saveAll(any())).thenReturn(questions);

        mockMvc.perform(
                post("/question/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtos))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is("Question 1?")))
                .andExpect(jsonPath("$[0].solution", is("S1")))
                .andExpect(jsonPath("$[0].type", is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].sharable", is(false)))
                .andExpect(jsonPath("$[0].correctAnswer", is("True")))
                .andExpect(jsonPath("$[0].alternatives", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is("Question 2?")))
                .andExpect(jsonPath("$[1].solution", is("S2")))
                .andExpect(jsonPath("$[1].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].sharable", is(false)))
                .andExpect(jsonPath("$[1].correctAnswer", is("A")))
                .andExpect(jsonPath("$[1].alternatives", hasSize(3)));
    }

    @Test
    public void update_whenAllFieldsAreValid_shouldReturnAQuestionUpdated() throws Exception {
        QuestionDTO dto = QuestionDTO.builder()
                .id(1L)
                .solution("Solution")
                .statement("Statement")
                .type(QuestionType.TRUE_OR_FALSE)
                .active(true)
                .sharable(true)
                .correctAnswer("True")
                .alternatives(
                        List.of(
                                AlternativeDTO.builder()
                                        .description("True")
                                        .build(),
                                AlternativeDTO.builder()
                                        .description("False")
                                        .build()))
                .build();

        Question question = dto.toBuilder()
                .solution("New Solution")
                .statement("New Statement")
                .sharable(false)
                .correctAnswer("False")
                .build();

        when(questionService.update(any())).thenReturn(question);

        mockMvc.perform(
                put("/question")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
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
        doNothing().when(questionService).delete(1L);

        mockMvc.perform(
                delete("/question/1")).andExpect(status().isNoContent());
    }

    private List<QuestionDTO> getDtoList() {
        return List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .statement("Question 1?")
                        .solution("S1")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .active(true)
                        .sharable(false)
                        .correctAnswer("True")
                        .topic("T01")
                        .alternatives(
                                List.of(
                                        AlternativeDTO.builder()
                                                .description("True")
                                                .build(),
                                        AlternativeDTO.builder()
                                                .description("False")
                                                .build()))
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .statement("Question 2?")
                        .solution("S2")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("A")
                        .topic("T02")
                        .alternatives(getAlternatives())
                        .active(true)
                        .sharable(false)
                        .build());
    }

    private List<AlternativeDTO> getAlternatives(){
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
