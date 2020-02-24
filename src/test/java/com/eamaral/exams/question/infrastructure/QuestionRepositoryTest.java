package com.eamaral.exams.question.infrastructure;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.jpa.entity.MultipleChoiceEntity;
import com.eamaral.exams.question.infrastructure.jpa.entity.TrueOrFalseEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class QuestionRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private QuestionRepository repository;

    @Test
    public void save_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Question question = getTrueOrFalseQuestion();

        Question result = repository.save(question);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void saveAll_whenFieldsAreValid_shouldReturnQuestionsWithIds() {
        List<Question> questions = getQuestions();

        List<Question> result = repository.saveAll(questions);

        assertThat(result)
                .extracting("id")
                .isNotNull();
    }

    @Test
    public void findAll_whenQuestionsExist_shouldReturnAllQuestions() {
        List<Question> questions = getQuestions();

        repository.saveAll(questions);

        List<Question> result = repository.findAll();

        assertThat(result)
                .extracting("statement", "type", "correctAnswer")
                .containsExactlyInAnyOrder(
                        tuple("Can I test TF?", QuestionType.TRUE_OR_FALSE, "True"),
                        tuple("Can I test MC?", QuestionType.MULTIPLE_CHOICES, "B"));
    }

    @Test
    public void findById_whenIdExistsAndQuestionIsTrueOrFalse_shouldReturnATrueOrFalseQuestion() {
        Question question = getTrueOrFalseQuestion();

        question = repository.save(question);

        Question result = repository.findById(question.getId());

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

        question = repository.save(question);

        Question result = repository.findById(question.getId());

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
                () -> repository.findById(1L),
                "Question 1 not found")
                .isInstanceOf(Exception.class);
    }

    @Test
    public void save_whenQuestionTypeIsInvalid_shouldThrowsException() {
        assertThatThrownBy(
                () -> repository.save(MultipleChoiceEntity.builder().build()),
                "Question type informed is invalid")
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    public void update_shouldUpdateTheFieldsOfAQuestion(){
        Question question = getMultipleChoice();
        question = repository.save(question);

        MultipleChoiceEntity entity = MultipleChoiceEntity.builder()
                .id(question.getId())
                .alternatives(AlternativeEntity.from(question.getAlternatives()))
                .type(question.getType())
                .correctAnswer("World")
                .statement("Hello")
                .sharable(false)
                .solution("Hello World!")
                .topic("Greetings")
                .build();

        question = repository.save(entity);

        assertThat(question)
                .extracting(
                        "id",
                        "statement",
                        "solution",
                        "correctAnswer",
                        "topic",
                        "sharable")
                .containsExactlyInAnyOrder(
                        entity.getId(),
                        "Hello",
                        "Hello World!",
                        "World",
                        "Greetings",
                        false);
    }

    @Test
    public void findByStatement_whenQuestionWithTheStatementExists_shouldReturnAQuestionByItsExactlyStatement() {
        Question question = getMultipleChoice();
        repository.save(question);

        Optional<Question> result = repository.findByStatement(question.getStatement());

        assertThat(result).isPresent();
    }

    @Test
    public void findByStatement_whenStatementDoesNotExist_shouldReturnEmpty() {
        Optional<Question> result = repository.findByStatement("Hello World!");

        assertThat(result).isEmpty();
    }

    @Test
    public void delete_shouldRemoveAQuestion() {
        Question question = getMultipleChoice();
        question = repository.save(question);

        List<Question> questions = repository.findAll();

        assertThat(questions).hasSize(1);

        repository.delete(question.getId());

        questions = repository.findAll();

        assertThat(questions).hasSize(0);
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
                .active(true)
                .build();
    }

    private MultipleChoiceEntity getMultipleChoice() {
        return MultipleChoiceEntity.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .active(true)
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