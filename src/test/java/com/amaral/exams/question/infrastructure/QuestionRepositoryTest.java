package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.services.Question;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class QuestionRepositoryTest extends JPAIntegrationTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void save_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Question question = QuestionData.builder()
                .statement("Can I test it?")
                .type(QuestionType.TRUE_OR_FALSE)
                .build();

        Question result = questionRepository.save(question);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void saveAll_whenFieldsAreValid_shouldReturnQuestionsWithIds() {
        List<Question> questions = List.of(
                QuestionData.builder()
                        .statement("Can I test TF?")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .build(),
                QuestionData.builder()
                        .statement("Can I test MC?").
                        type(QuestionType.MULTIPLE_CHOICES)
                        .build());

        List<Question> result = questionRepository.saveAll(questions);

        assertThat(result)
                .extracting("id")
                .isNotNull();
    }

    @Test
    public void findAll_whenQuestionsExist_shouldReturnAllQuestions() {
        List<Question> questions = List.of(
                QuestionData.builder()
                        .statement("Can I test TF?")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .build(),
                QuestionData.builder()
                        .statement("Can I test MC?").
                        type(QuestionType.MULTIPLE_CHOICES)
                        .build());

        questionRepository.saveAll(questions);

        List<Question> result = questionRepository.findAll();

        assertThat(result)
                .extracting("statement", "type")
                .containsExactlyInAnyOrder(
                        tuple("Can I test TF?", QuestionType.TRUE_OR_FALSE),
                        tuple("Can I test MC?", QuestionType.MULTIPLE_CHOICES));
    }

    @Test
    public void findById_whenIdExists_shouldReturnAQuestion() {
        Question question = QuestionData.builder()
                .statement("Can I test it?")
                .type(QuestionType.TRUE_OR_FALSE)
                .build();

        question = questionRepository.save(question);

        Question result = questionRepository.findById(question.getId());

        assertThat(result)
                .extracting("statement", "type")
                .containsExactly("Can I test it?", QuestionType.TRUE_OR_FALSE);
    }

    @Test
    public void findById_whenIdDoesNotExist_shouldReturnANotFoundException() {
        assertThatThrownBy(
                () -> questionRepository.findById(1L),
                "Question 1 not found");
    }
}