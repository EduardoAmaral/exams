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
                Question.builder()
                        .statement("ABC")
                        .build(),
                Question.builder()
                        .statement("XYZ")
                        .build());

        when(questionService.findAll()).thenReturn(questions);

        mockMvc.perform(
                get("/question"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].statement", is("ABC")))
                .andExpect(jsonPath("$[1].statement", is("XYZ")));;
    }

    @Test
    public void getById_whenQuestionExists_shouldReturnAQuestion() throws Exception {
        when(questionService.findById(1L)).thenReturn(Question.builder().statement("ABC").build());

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

        Question question = Question.builder()
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
}
