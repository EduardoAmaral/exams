package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.configuration.exception.ForbiddenException;
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

    @Test
    public void findByUser_shouldReturnAllQuestionsCreatedByTheUser() {
        String author = "1";
        String statement1 = "AAA";
        String statement2 = "EEE";
        List<Question> questions = List.of(
                QuestionDTO.builder()
                        .statement(statement1)
                        .author(author)
                        .build(),
                QuestionDTO.builder()
                        .statement(statement2)
                        .author(author)
                        .build());

        when(repositoryPort.findByUser(author)).thenReturn(questions);

        List<Question> result = service.findByUser(author);

        assertThat(result)
                .extracting(Question::getStatement, Question::getAuthor)
                .containsOnly(
                        tuple(statement1, author),
                        tuple(statement2, author));
    }

    @Test
    public void findById_shouldReturnAQuestion() {
        Question question = getQuestionBuilder("A", "Statement", "True").build();

        String questionId = "1";
        when(repositoryPort.find(questionId)).thenReturn(Optional.of(question));

        Question result = service.find(questionId);

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
        String questionId = "1";
        when(repositoryPort.find(questionId)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.find(questionId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Question 1 not found");
    }

    @Test
    public void findById_whenIdIsNull_shouldThrowException() {
        assertThatThrownBy(
                () -> service.find(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Question's id is required");
    }

    @Test
    public void save_shouldReturnAQuestion() {
        Question question = QuestionDTO.builder()
                .statement("AAA")
                .build();

        when(repositoryPort.save(question)).then(invocation -> {
            Question q = invocation.getArgument(0);
            return QuestionDTO.builder()
                    .id("1")
                    .statement(q.getStatement())
                    .build();
        });

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
                            .id(String.valueOf(request.indexOf(q) + 1))
                            .statement(q.getStatement())
                            .build())
                    .collect(toList());
        });

        List<Question> result = service.saveAll(request);

        assertThat(result)
                .extracting(Question::getId, Question::getStatement)
                .containsExactlyInAnyOrder(
                        tuple("1", "AAA"),
                        tuple("2", "EEE")
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
                .hasMessage("Question's type cannot be updated");
    }

    @Test
    public void update_whenQuestionDoesntExist_shouldThrowNotFoundException() {
        String questionId = "1";
        Question question = QuestionDTO.builder().id(questionId).build();

        when(repositoryPort.find(questionId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.update(question))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Question 1 not found");
    }

    @Test
    public void update_whenUserIsDifferentFromTheCreator_shouldThrowForbiddenException() {
        String questionId = "1";
        QuestionDTO.QuestionDTOBuilder builder = QuestionDTO.builder()
                .id(questionId)
                .statement("A")
                .type(QuestionType.MULTIPLE_CHOICES)
                .author("123");
        Question question = builder
                .build();

        String savedUserId = "456";
        when(repositoryPort.find(questionId))
                .thenReturn(Optional.of(
                        builder.author(savedUserId)
                                .build()));

        Assertions.assertThatThrownBy(() -> service.update(question))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Questions's user id can't be different from the question's creator");
    }

    @Test
    public void delete_shouldCallDeleteMethodFromRepository() {
        Question question = getQuestionBuilder("A", "B", "C").build();
        String questionId = "1";

        when(repositoryPort.find(questionId)).thenReturn(Optional.of(question));
        doNothing().when(repositoryPort).delete(question);

        service.delete(questionId, "1");

        verify(repositoryPort).delete(question);
    }

    @Test
    public void delete_whenQuestionDoesntExist_shouldReturnNotFoundException() {
        when(repositoryPort.find(anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.delete("1", "1"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Question 1 not found");
    }

    @Test
    public void delete_whenUserIsDifferentFromTheCreator_shouldReturnForbiddenException() {
        String questionId = "1";
        String currentUserId = "1";
        String savedUserId = "456";

        when(repositoryPort.find(questionId))
                .thenReturn(Optional.of(
                        QuestionDTO.builder()
                                .author(savedUserId)
                                .build()));

        Assertions.assertThatThrownBy(() -> service.delete(questionId, currentUserId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Questions's user id can't be different from the question's creator");
    }

    @Test
    public void search_shouldCallSearchByCriteria() {
        String currentUser = "1";
        when(repositoryPort.findByCriteria(any(), eq(currentUser))).thenReturn(emptyList());

        service.search(QuestionDTO.builder().build(), currentUser);

        verify(repositoryPort).findByCriteria(any(), eq(currentUser));
    }

    private QuestionDTO.QuestionDTOBuilder getQuestionBuilder(String solution,
                                                              String statement,
                                                              String correctAnswer) {
        return QuestionDTO.builder()
                .id("1")
                .solution(solution)
                .statement(statement)
                .type(QuestionType.TRUE_OR_FALSE)
                .sharable(false)
                .correctAnswer(correctAnswer)
                .author("1")
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
