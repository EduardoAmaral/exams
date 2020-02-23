package com.amaral.exams.question.domain.services;

import com.amaral.exams.configuration.exception.InvalidDataException;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.application.dto.AlternativeDTO;
import com.amaral.exams.question.application.dto.QuestionDTO;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.domain.services.services.QuestionService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Test
    public void update_whenTypeDoesNotChange_shouldReturnAUpdatedQuestion() {
        Question question = getQuestionBuilder("Solution", "Statement", "False")
                .build();

        Question response = getQuestionBuilder("New Solution", "New Statement", "True")
                .build();

        when(repositoryPort.findById(question.getId())).thenReturn(question);
        when(repositoryPort.save(question)).thenReturn(response);

        Question result = service.update(question);

        assertThat(result)
                .extracting("solution", "statement", "correctAnswer")
                .containsExactlyInAnyOrder("New Solution", "New Statement", "True");
    }

    @Test
    public void update_whenTypeChanges_shouldReturnAInvalidException() {
        QuestionDTO.QuestionDTOBuilder builder = getQuestionBuilder("Solution", "Statement", "False");
        Question question = builder.type(QuestionType.TRUE_OR_FALSE).build();


        when(repositoryPort.findById(question.getId())).thenReturn(builder.type(QuestionType.MULTIPLE_CHOICES).build());

        Assertions.assertThatThrownBy(() -> service.update(question), "Question's type cannot be updated")
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    public void delete_shouldCallDeleteMethodFromRepository() {
        doNothing().when(repositoryPort).delete(1L);

        service.delete(1L);

        verify(repositoryPort, atLeastOnce()).delete(1L);
    }

    private QuestionDTO.QuestionDTOBuilder getQuestionBuilder(String solution, String statement, String correctAnswer) {
        return QuestionDTO.builder()
                .id(1L)
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .active(true)
                .sharable(false)
                .correctAnswer(correctAnswer)
                .alternatives(
                        List.of(
                                AlternativeDTO.builder()
                                        .description("True")
                                        .build(),
                                AlternativeDTO.builder()
                                        .description("False")
                                        .build()));
    }
}
