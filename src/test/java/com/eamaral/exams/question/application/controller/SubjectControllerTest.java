package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubjectControllerTest extends ControllerIntegrationTest {

    public static final String ENDPOINT = "/api/question/subject";

    @Test
    public void get_shouldReturnAllSubjects() throws Exception {
        when(subjectPort.findAll()).thenReturn(new ArrayList<>(List.of(
                SubjectDTO.builder()
                        .id("1")
                        .description("English")
                        .build(),
                SubjectDTO.builder()
                        .id("2")
                        .description("French")
                        .build()
        )));

        mockMvc.perform(
                get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is("English")))
                .andExpect(jsonPath("$[1].description", is("French")));
    }

    @Test
    public void save_shouldReturnCreated() throws Exception {
        SubjectDTO dto = SubjectDTO.builder()
                .description("English")
                .build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void save_whenDescriptionIsBlank_shouldReturnCreated() throws Exception {
        SubjectDTO dto = SubjectDTO.builder()
                .description("      ")
                .build();

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
