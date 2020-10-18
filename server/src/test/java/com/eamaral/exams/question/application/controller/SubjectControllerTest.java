package com.eamaral.exams.question.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

class SubjectControllerTest extends ControllerIntegrationTest {

    private static final String ENDPOINT = "/api/question/subject";

    @Test
    @DisplayName("should retrieve all subjects")
    void list_shouldReturnAllSubjects() throws Exception {
        when(subjectService.findAll()).thenReturn(new ArrayList<>(List.of(
                Subject.builder()
                        .id(1L)
                        .description("English")
                        .build(),
                Subject.builder()
                        .id(2L)
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
    @DisplayName("should retrieve the subject saved when creating a subject")
    void save_shouldReturnCreated() throws Exception {
        SubjectDTO dto = SubjectDTO.builder()
                .description("English")
                .build();

        when(subjectService.save(dto.toDomain())).thenReturn(Subject.builder()
                .id(1L)
                .description("English")
                .build());

        mockMvc.perform(
                post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("English")));
    }

    @Test
    @DisplayName("should return bad request when creating a subject without description")
    void save_whenDescriptionIsBlank_shouldReturnCreated() throws Exception {
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
