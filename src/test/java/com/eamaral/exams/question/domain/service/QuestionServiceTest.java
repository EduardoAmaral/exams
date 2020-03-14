package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.AlternativeDTO;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.application.dto.SubjectDTO;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.port.QuestionRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService service;

    @Mock
    private QuestionRepositoryPort repositoryPort;

    @Test
    public void findByUser_shouldReturnAllQuestionsCreatedByTheUser() {
        String userId = "1";
        String statement1 = "AAA";
        String statement2 = "EEE";
        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .statement(statement1)
                        .userId(userId)
                        .build(),
                QuestionDTO.builder()
                        .statement(statement2)
                        .userId(userId)
                        .build());

        when(repositoryPort.findByUser(userId)).thenReturn(questions);

        List<Question> result = service.findByUser(userId);

        assertThat(result)
                .extracting(Question::getStatement, Question::getUserId)
                .containsOnly(
                        tuple(statement1, userId),
                        tuple(statement2, userId));
    }

    @Test
    public void findById_shouldReturnAQuestion() {
        Question question = getQuestionBuilder("A", "Statement", "True").build();

        when(repositoryPort.find(1L)).thenReturn(Optional.of(question));

        Question result = service.find(1L);

        assertThat(result)
                .extracting(Question::getId,
                        Question::getStatement,
                        Question::getType,
                        q -> q.getSubject().getDescription())
                .containsExactly(1L,
                        "Statement",
                        QuestionType.TRUE_OR_FALSE,
                        "English");
    }

    @Test
    public void findById_whenQuestionDoesntExist_shouldThrowsException() {
        when(repositoryPort.find(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.find(1L),
                "Question 1 not found")
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void save_shouldReturnAQuestion() {
        Question question = QuestionDTO.builder()
                .statement("AAA")
                .build();

        when(repositoryPort.save(question)).then(invocation -> {
            Question q = invocation.getArgument(0);
            return QuestionDTO.builder()
                    .id(1L)
                    .statement(q.getStatement())
                    .build();
        });

        Question result = service.save(question);

        assertThat(result)
                .extracting(Question::getId, Question::getStatement)
                .containsExactly(1L, "AAA");
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

        when(repositoryPort.saveAll(request)).then(invocation -> {
            List<Question> args = invocation.getArgument(0);
            return args.stream().map(q ->
                    QuestionDTO.builder()
                            .id(request.indexOf(q) + 1L)
                            .statement(q.getStatement())
                            .build())
                    .collect(toList());
        });

        List<Question> result = service.saveAll(request);

        assertThat(result)
                .extracting(Question::getId, Question::getStatement)
                .containsExactlyInAnyOrder(
                        tuple(1L, "AAA"),
                        tuple(2L, "EEE")
                );
    }

    @Test
    public void update_whenTypeDoesNotChange_shouldReturnAUpdatedQuestion() {
        Question question = getQuestionBuilder("Solution", "Statement", "False")
                .build();

        Question response = getQuestionBuilder("New Solution", "New Statement", "True")
                .build();

        when(repositoryPort.find(question.getId())).thenReturn(Optional.of(question));
        when(repositoryPort.save(question)).thenReturn(response);

        Question result = service.update(question);

        assertThat(result)
                .extracting(
                        Question::getStatement,
                        Question::getSolution,
                        Question::getCorrectAnswer)
                .containsExactlyInAnyOrder(
                        "New Statement",
                        "New Solution",
                        "True");
    }

    @Test
    public void update_whenTypeChanges_shouldReturnAInvalidException() {
        QuestionDTO.QuestionDTOBuilder builder = getQuestionBuilder("Solution", "Statement", "False");
        Question question = builder.type(QuestionType.TRUE_OR_FALSE).build();


        when(repositoryPort.find(question.getId()))
                .thenReturn(Optional.of(builder.type(QuestionType.MULTIPLE_CHOICES)
                        .build()));

        Assertions.assertThatThrownBy(() -> service.update(question))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("{question.invalid.type.update}");
    }

    @Test
    public void update_whenQuestionDoesntExist_shouldReturnNotFoundException() {
        Question question = QuestionDTO.builder().id(1L).build();

        when(repositoryPort.find(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(question))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("{question.not.found}");
    }

    @Test
    public void delete_shouldCallDeleteMethodFromRepository() {
        Question question = QuestionDTO.builder().id(1L).build();

        when(repositoryPort.find(1L)).thenReturn(Optional.of(question));
        doNothing().when(repositoryPort).delete(question);

        service.delete(1L, "1");

        verify(repositoryPort).delete(question);
    }

    @Test
    public void delete_whenQuestionDoesntExist_shouldReturnNotFoundException() {
        when(repositoryPort.find(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete(1L, "1"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("{question.not.found}");
    }

    private QuestionDTO.QuestionDTOBuilder getQuestionBuilder(String solution,
                                                              String statement,
                                                              String correctAnswer) {
        return QuestionDTO.builder()
                .id(1L)
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .active(true)
                .sharable(false)
                .correctAnswer(correctAnswer)
                .subject(SubjectDTO.builder()
                        .description("English")
                        .build())
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
