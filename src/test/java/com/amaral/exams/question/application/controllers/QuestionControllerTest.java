package com.amaral.exams.question.application.controllers;

import com.amaral.exams.configuration.controller.ControllerIntegrationTest;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.application.dto.QuestionDTO;
import com.amaral.exams.question.domain.services.Question;
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
                .type(QuestionType.MULTIPLE_CHOICES)
                .active(true)
                .sharable(false)
                .build();

        Question question = QuestionDTO.builder()
                .id(1L)
                .solution(solution)
                .statement(statement)
                .type(QuestionType.MULTIPLE_CHOICES)
                .active(true)
                .sharable(false)
                .build();

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
                .andExpect(jsonPath("$.type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.sharable", is(false)));
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
                        .active(true)
                        .sharable(false)
                        .build(),
                QuestionDTO.builder()
                        .solution(solution2)
                        .statement(statement2)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .active(true)
                        .sharable(false)
                        .build());

        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .solution(solution1)
                        .statement(statement1)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .active(true)
                        .sharable(false)
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .solution(solution2)
                        .statement(statement2)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .active(true)
                        .sharable(false)
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
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].statement", is(statement2)))
                .andExpect(jsonPath("$[1].solution", is(solution2)))
                .andExpect(jsonPath("$[1].type", is(QuestionType.MULTIPLE_CHOICES.toString())))
                .andExpect(jsonPath("$[1].active", is(true)))
                .andExpect(jsonPath("$[1].sharable", is(false)));
    }
}
