package com.amaral.exams.question.application.controllers;

import com.amaral.exams.configuration.controller.ControllerIntegrationTest;
import com.amaral.exams.question.application.dto.SubjectDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubjectControllerTest extends ControllerIntegrationTest {

    @Test
    public void get_shouldReturnAllSubjects() throws Exception {
        when(subjectService.findAll()).thenReturn(new ArrayList<>(List.of(
                SubjectDTO.builder()
                        .id(1L)
                        .description("English")
                        .build(),
                SubjectDTO.builder()
                        .id(2L)
                        .description("French")
                        .build()
                )));

        mockMvc.perform(
                get("/question/subject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is("English")))
                .andExpect(jsonPath("$[1].description", is("French")));
    }
}
