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

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService service;

    @Mock
    private QuestionRepositoryPort repositoryPort;

    private String currentUser = "1";

    private final Long questionId = 1L;

    @Test
    public void findByUser_shouldReturnAllQuestionsCreatedByTheUser() {
        String statement1 = "AAA";
        String statement2 = "EEE";
        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .statement(statement1)
                        .author(currentUser)
                        .build(),
                QuestionDTO.builder()
                        .statement(statement2)
                        .author(currentUser)
                        .build());

        when(repositoryPort.findByUser(currentUser)).thenReturn(questions);

        List<Question> result = service.findByUser(currentUser);

        assertThat(result)
                .extracting(Question::getStatement, Question::getAuthor)
                .containsOnly(
                        tuple(statement1, currentUser),
                        tuple(statement2, currentUser));
    }

    @Test
    public void findById_shouldReturnAQuestion() {
        Question question = getQuestionBuilder("A", "Statement", "True").build();

        when(repositoryPort.find(questionId, currentUser)).thenReturn(Optional.of(question));

        Question result = service.find(questionId, currentUser);

        assertThat(result)
                .extracting(Question::getId,
                        Question::getStatement,
                        Question::getType,
                        q -> q.getSubject().getDescription())
                .containsExactly(questionId,
                        "Statement",
                        QuestionType.TRUE_OR_FALSE,
                        "English");
    }

    @Test
    public void findById_whenQuestionDoesntExist_shouldThrowNotFoundException() {
        when(repositoryPort.find(questionId, currentUser)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.find(questionId, currentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Question's %s doesn't exist or it's not accessible to the user %s", questionId, currentUser));
    }

    @Test
    public void findById_whenIdIsNull_shouldThrowException() {
        assertThatThrownBy(
                () -> service.find(null, currentUser))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Question's id is required");
    }

    @Test
    public void save_shouldReturnAQuestion() {
        Question question = QuestionDTO.builder()
                .statement("AAA")
                .correctAnswer("True")
                .alternatives(getAlternatives())
                .build();

        when(repositoryPort.save(question)).thenReturn(any());

        service.save(question);

        verify(repositoryPort).save(question);
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

        when(repositoryPort.find(question.getId(), currentUser)).thenReturn(Optional.of(question));
        when(repositoryPort.save(question)).thenReturn(response);

        Question result = service.update(question, currentUser);

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

        when(repositoryPort.find(question.getId(), currentUser))
                .thenReturn(Optional.of(builder.type(QuestionType.MULTIPLE_CHOICES)
                        .build()));

        Assertions.assertThatThrownBy(() -> service.update(question, currentUser))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Question's type can't be updated");
    }

    @Test
    public void update_whenQuestionDoesntExist_shouldThrowNotFoundException() {
        Question question = QuestionDTO.builder().id(questionId).build();

        when(repositoryPort.find(questionId, currentUser))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(question, currentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Question's %s doesn't exist or it's not accessible to the user %s", questionId, currentUser));
    }

    @Test
    public void update_whenUserIsDifferentFromTheCreator_shouldThrowForbiddenException() {
        QuestionDTO.QuestionDTOBuilder builder = QuestionDTO.builder()
                .id(questionId)
                .statement("A")
                .correctAnswer("True")
                .alternatives(getAlternatives())
                .type(QuestionType.TRUE_OR_FALSE)
                .author("123");
        Question question = builder
                .build();

        when(repositoryPort.find(questionId, currentUser))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(question, currentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Question's %s doesn't exist or it's not accessible to the user %s", questionId, currentUser));
    }

    @Test
    public void update_whenCorrectAnswerDoesntMatchAnyOfTheAlternatives_shouldThrowInvalidDataException() {
        Question question = QuestionDTO.builder()
                .id(questionId)
                .statement("A")
                .correctAnswer("Wrong")
                .alternatives(getAlternatives())
                .type(QuestionType.TRUE_OR_FALSE)
                .author("123").build();

        when(repositoryPort.find(questionId, currentUser))
                .thenReturn(Optional.of(question));

        Assertions.assertThatThrownBy(() -> service.update(question, currentUser))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("The correct answer to the question must be one of your alternatives");
    }

    @Test
    public void delete_shouldCallDeleteMethodFromRepository() {
        Question question = getQuestionBuilder("A", "B", "C").build();

        when(repositoryPort.find(questionId, currentUser)).thenReturn(Optional.of(question));
        doNothing().when(repositoryPort).delete(question);

        service.delete(questionId, "1");

        verify(repositoryPort).delete(question);
    }

    @Test
    public void delete_whenQuestionDoesntExist_shouldReturnNotFoundException() {
        when(repositoryPort.find(anyLong(), eq(currentUser)))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete(questionId, currentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Question's %s doesn't exist or it's not accessible to the user %s", questionId, currentUser));
    }

    @Test
    public void search_shouldCallSearchByCriteria() {
        when(repositoryPort.findByCriteria(any(), eq(currentUser))).thenReturn(emptyList());

        service.search(QuestionDTO.builder().build(), currentUser);

        verify(repositoryPort).findByCriteria(any(), eq(currentUser));
    }

    @Test
    public void save_whenCorrectAnswerDoesntMatchAnyOfTheAlternatives_toTrueOrFalseQuestion_shouldThrowInvalidDataException() {
        Question question = QuestionDTO.builder()
                .correctAnswer("Wrong")
                .type(QuestionType.TRUE_OR_FALSE)
                .build();

        assertThatExceptionOfType(InvalidDataException.class).isThrownBy(() -> service.save(question))
                .withMessage("The correct answer to the question must be one of your alternatives");
    }

    @Test
    public void save_whenCorrectAnswerDoesntMatchAnyOfTheAlternatives_toMultipleChoicesQuestion_shouldThrowInvalidDataException() {
        Question question = QuestionDTO.builder()
                .correctAnswer("Wrong")
                .type(QuestionType.MULTIPLE_CHOICES)
                .alternatives(getAlternatives())
                .build();

        assertThatExceptionOfType(InvalidDataException.class).isThrownBy(() -> service.save(question))
                .withMessage("The correct answer to the question must be one of your alternatives");
    }

    private QuestionDTO.QuestionDTOBuilder getQuestionBuilder(String solution,
                                                              String statement,
                                                              String correctAnswer) {
        return QuestionDTO.builder()
                .id(questionId)
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .shared(false)
                .correctAnswer(correctAnswer)
                .author(currentUser)
                .subject(SubjectDTO.builder()
                        .description("English")
                        .build())
                .alternatives(
                        getAlternatives());
    }


    private List<AlternativeDTO> getAlternatives() {
        return List.of(AlternativeDTO.builder()
                        .description("True")
                        .build(),
                AlternativeDTO.builder()
                        .description("False")
                        .build());
    }
}
