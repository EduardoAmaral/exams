package com.amaral.exams.question.application.controllers;

import com.amaral.exams.configuration.controller.ControllerIntegrationTest;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.application.dto.AlternativeDTO;
import com.amaral.exams.question.application.dto.QuestionDTO;
import com.amaral.exams.question.domain.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest extends ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void get_shouldReturnAllQuestions() throws Exception {
        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .statement("ABC")
                        .build(),
                QuestionDTO.builder()
                        .statement("XYZ")
                        .build());

        when(questionService.findAll()).thenReturn(questions);

        mockMvc.perform(
                get("/question"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].statement", is("ABC")))
                .andExpect(jsonPath("$[1].statement", is("XYZ")));
    }

    @Test
    public void getById_whenQuestionExists_shouldReturnAQuestion() throws Exception {
        when(questionService.findById(1L)).thenReturn(QuestionDTO.builder().statement("ABC").build());

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.statement", is(statement)))
                .andExpect(jsonPath("$.solution", is(solution)))
                .andExpect(jsonPath("$.type", is(QuestionType.TRUE_OR_FALSE.toString())))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.sharable", is(false)))
                .andExpect(jsonPath("$.correctAnswer", is("A")))
                .andExpect(jsonPath("$.sharable", is(false)))
                .andExpect(jsonPath("$.alternatives[0].description", is("True")))
                .andExpect(jsonPath("$.alternatives[1].description", is("False")));
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
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createByList_whenAllFieldsAreValid_shouldReturnQuestionsWithId() throws Exception {
        String statement1 = "Question 1?";
        String solution1 = "S1";
        String statement2 = "Question 2?";
        String solution2 = "S2";

        List<QuestionDTO> dtos = List.of(
                QuestionDTO.builder()
                        .solution(solution1)
                        .statement(statement1)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("C")
                        .alternatives(getAlternatives())
                        .active(true)
                        .sharable(false)
                        .build(),
                QuestionDTO.builder()
                        .solution(solution2)
                        .statement(statement2)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("A")
                        .alternatives(getAlternatives())
                        .active(true)
                        .sharable(false)
                        .build());

        List<Question> questions = List.of(
                dtos.get(0)
                        .toBuilder()
                        .id(1L)
                        .build(),
                dtos.get(1)
                        .toBuilder()
                        .id(2L)
                        .build());

        when(questionService.saveAll(any())).thenReturn(questions);

        mockMvc.perform(
                post("/question/list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtos))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is(statement1)))
                .andExpect(jsonPath("$[0].solution", is(solution1)))
                .andExpect(jsonPath("$[0].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].sharable", is(false)))
                .andExpect(jsonPath("$[0].correctAnswer", is("C")))
                .andExpect(jsonPath("$[0].alternatives", hasSize(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is(statement2)))
                .andExpect(jsonPath("$[1].solution", is(solution2)))
                .andExpect(jsonPath("$[1].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].sharable", is(false)))
                .andExpect(jsonPath("$[1].correctAnswer", is("A")))
                .andExpect(jsonPath("$[1].alternatives", hasSize(3)));
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
