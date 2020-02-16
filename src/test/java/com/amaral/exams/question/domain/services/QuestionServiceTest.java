package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.domain.services.services.QuestionService;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
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
                QuestionData.builder()
                        .statement("AAA")
                        .build(),
                QuestionData.builder()
                        .statement("EEE")
                        .build());

        when(repositoryPort.findAll()).thenReturn(questions);

        List<Question> result = service.findAll();

        assertThat(result).extracting("statement").containsOnly("AAA", "EEE");
    }

    @Test
    public void findById_shouldReturnAQuestion() {
        Question question = QuestionData.builder()
                .statement("AAA")
                .build();

        when(repositoryPort.findById(1L)).thenReturn(question);

        Question result = service.findById(1L);

        assertThat(result).isEqualTo(question);
    }
}
