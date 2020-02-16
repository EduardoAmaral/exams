package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.application.dto.QuestionDTO;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.domain.services.services.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService service;

    @Mock
    private QuestionRepositoryPort repositoryPort;

    @Test
    public void findAll_shouldReturnAllQuestions() {
        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .statement("AAA")
                        .build(),
                QuestionDTO.builder()
                        .statement("EEE")
                        .build());

        when(repositoryPort.findAll()).thenReturn(questions);

        List<Question> result = service.findAll();

        assertThat(result).extracting("statement").containsOnly("AAA", "EEE");
    }

    @Test
    public void findById_shouldReturnAQuestion() {
        Question question = QuestionDTO.builder()
                .statement("AAA")
                .build();

        when(repositoryPort.findById(1L)).thenReturn(question);

        Question result = service.findById(1L);

        assertThat(result).isEqualTo(question);
    }

    @Test
    public void save_shouldReturnAQuestion() {
        Question question = QuestionDTO.builder()
                .id(1L)
                .statement("AAA")
                .build();

        when(repositoryPort.save(question)).thenReturn(question);

        Question result = service.save(question);

        assertThat(result).isNotNull();
    }

    @Test
    public void saveAll_shouldReturnAllQuestionsWithId() {
        List<Question> request = List.of(
                QuestionDTO.builder()
                        .statement("AAA")
                        .build(),
                QuestionDTO.builder()
                        .statement("EEE")
                        .build());

        List<Question> response = List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .statement("AAA")
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .statement("EEE")
                        .build());

        when(repositoryPort.saveAll(request)).thenReturn(response);

        List<Question> result = service.saveAll(request);

        assertThat(result).extracting("id").isNotNull();
    }
}
