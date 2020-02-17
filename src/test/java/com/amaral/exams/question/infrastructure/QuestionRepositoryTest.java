package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.exception.DataNotFoundException;
import com.amaral.exams.configuration.exception.InvalidQuestionTypeException;
import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Question;
import com.amaral.exams.question.infrastructure.jpa.AlternativeEntity;
import com.amaral.exams.question.infrastructure.jpa.MultipleChoiceEntity;
import com.amaral.exams.question.infrastructure.jpa.TrueOrFalseEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class QuestionRepositoryTest extends JPAIntegrationTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void save_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Question question = getTrueOrFalseQuestion();

        Question result = questionRepository.save(question);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void saveAll_whenFieldsAreValid_shouldReturnQuestionsWithIds() {
        List<Question> questions = getQuestions();

        List<Question> result = questionRepository.saveAll(questions);

        assertThat(result)
                .extracting("id")
                .isNotNull();
    }

    @Test
    public void findAll_whenQuestionsExist_shouldReturnAllQuestions() {
        List<Question> questions = getQuestions();

        questionRepository.saveAll(questions);

        List<Question> result = questionRepository.findAll();

        assertThat(result)
                .extracting("statement", "type", "correctAnswer")
                .containsExactlyInAnyOrder(
                        tuple("Can I test TF?", QuestionType.TRUE_OR_FALSE, "True"),
                        tuple("Can I test MC?", QuestionType.MULTIPLE_CHOICES, "B"));
    }

    @Test
    public void findById_whenIdExistsAndQuestionIsTrueOrFalse_shouldReturnATrueOrFalseQuestion() {
        Question question = getTrueOrFalseQuestion();

        question = questionRepository.save(question);

        Question result = questionRepository.findById(question.getId());

        assertThat(result)
                .extracting("statement", "type", "correctAnswer")
                .containsExactly("Can I test TF?", QuestionType.TRUE_OR_FALSE, "True");
        assertThat(result.getAlternatives())
                .extracting("description")
                .containsExactlyInAnyOrder("True", "False");
    }

    @Test
    public void findById_whenIdExistsAndQuestionIsMultipleChoice_shouldReturnAMultipleChoiceQuestion() {
        Question question = getMultipleChoice();

        question = questionRepository.save(question);

        Question result = questionRepository.findById(question.getId());

        assertThat(result)
                .extracting("statement", "type", "correctAnswer")
                .containsExactly("Can I test MC?", QuestionType.MULTIPLE_CHOICES, "B");
        assertThat(result.getAlternatives())
                .extracting("description")
                .containsExactlyInAnyOrder("A", "B", "C", "D", "E");
    }

    @Test
    public void findById_whenIdDoesNotExist_shouldThrowsNotFoundException() {
        assertThatThrownBy(
                () -> questionRepository.findById(1L),
                "Question 1 not found")
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    public void save_whenQuestionTypeIsInvalid_shouldThrowsException() {
        assertThatThrownBy(
                () -> questionRepository.save(MultipleChoiceEntity.builder().build()),
                "Question type informed is invalid")
                .isInstanceOf(InvalidQuestionTypeException.class);
    }

    private List<Question> getQuestions() {
        return List.of(
                getTrueOrFalseQuestion(),
                getMultipleChoice());
    }

    private TrueOrFalseEntity getTrueOrFalseQuestion() {
        return TrueOrFalseEntity.builder()
                .statement("Can I test TF?")
                .type(QuestionType.TRUE_OR_FALSE)
                .correctAnswer("True")
                .build();
    }

    private MultipleChoiceEntity getMultipleChoice() {
        return MultipleChoiceEntity.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .alternatives(getAlternatives())
                .build();
    }

    private List<AlternativeEntity> getAlternatives() {
        return List.of(
                AlternativeEntity.builder()
                        .description("A")
                        .build(),
                AlternativeEntity.builder()
                        .description("B")
                        .build(),
                AlternativeEntity.builder()
                        .description("C")
                        .build(),
                AlternativeEntity.builder()
                        .description("D")
                        .build(),
                AlternativeEntity.builder()
                        .description("E")
                        .build());
    }
}