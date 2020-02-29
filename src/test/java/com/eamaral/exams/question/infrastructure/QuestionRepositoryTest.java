package com.eamaral.exams.question.infrastructure;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.infrastructure.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.jpa.entity.MultipleChoiceEntity;
import com.eamaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import com.eamaral.exams.question.infrastructure.jpa.entity.TrueOrFalseEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class QuestionRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private SubjectRepository subjectRepository;

    private SubjectEntity subject;

    @Before
    public void setUp() {
        Subject english = SubjectEntity.builder()
                .description("English")
                .build();
        subject = SubjectEntity.from(subjectRepository.save(english));
    }

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
                .extracting(Question::getId)
                .isNotNull();
    }

    @Test
    public void findAll_whenQuestionsExist_shouldReturnAllQuestions() {
        List<Question> questions = getQuestions();

        repository.saveAll(questions);

        List<Question> result = repository.findAll();

        assertThat(result)
                .extracting(Question::getStatement,
                        Question::getType,
                        Question::getCorrectAnswer,
                        q -> q.getSubject().getDescription())
                .containsExactlyInAnyOrder(
                        tuple("Can I test TF?", QuestionType.TRUE_OR_FALSE, "True", "English"),
                        tuple("Can I test MC?", QuestionType.MULTIPLE_CHOICES, "B", "English"));
    }

    @Test
    public void findById_whenIdExistsAndQuestionIsTrueOrFalse_shouldReturnATrueOrFalseQuestion() {
        Question question = getTrueOrFalseQuestion();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId());

        assertThat(result).isPresent();

        assertThat(result.get())
                .extracting(Question::getStatement, Question::getType, Question::getCorrectAnswer)
                .containsExactly("Can I test TF?", QuestionType.TRUE_OR_FALSE, "True");

        assertThat(result.get().getAlternatives())
                .extracting(Alternative::getDescription)
                .containsExactlyInAnyOrder("True", "False");
    }

    @Test
    public void findById_whenIdExistsAndQuestionIsMultipleChoice_shouldReturnAMultipleChoiceQuestion() {
        Question question = getMultipleChoice();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId());

        assertThat(result).isPresent();

        assertThat(result.get())
                .extracting(Question::getStatement, Question::getType, Question::getCorrectAnswer)
                .containsExactly("Can I test MC?", QuestionType.MULTIPLE_CHOICES, "B");

        assertThat(result.get().getAlternatives())
                .extracting(Alternative::getDescription)
                .containsExactlyInAnyOrder("A", "B", "C", "D", "E");
    }

    @Test
    public void findById_whenIdDoesNotExist_shouldReturnEmpty() {
        Optional<Question> result = repository.find(1L);

        assertThat(result).isEmpty();
    }

    @Test
    public void save_whenQuestionTypeIsInvalid_shouldThrowsException() {
        assertThatThrownBy(
                () -> repository.save(MultipleChoiceEntity.builder().build()),
                "Question type informed is invalid")
                .isInstanceOf(InvalidDataException.class);
    }

    @Test
    public void update_shouldUpdateTheFieldsOfAQuestion() {
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
                .subject(subject)
                .build();

        question = repository.save(entity);

        assertThat(question)
                .extracting(
                        Question::getId,
                        Question::getStatement,
                        Question::getSolution,
                        Question::getCorrectAnswer,
                        Question::getTopic,
                        Question::isSharable,
                        q -> q.getSubject().getDescription())
                .containsExactlyInAnyOrder(
                        entity.getId(),
                        "Hello",
                        "Hello World!",
                        "World",
                        "Greetings",
                        false,
                        "English");
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

        repository.delete(question);

        questions = repository.findAll();

        assertThat(questions).hasSize(0);
    }

    @Test
    public void save_whenFieldsAreInvalid_shouldThrowsException() {
        Question question = MultipleChoiceEntity.builder()
                .subject(subject)
                .alternatives(Collections.emptyList())
                .correctAnswer("")
                .statement("")
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        assertThatThrownBy(() -> repository.save(question))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void save_whenSubjectIsBlank_shouldThrowsException() {
        Question question = MultipleChoiceEntity.builder()
                .subject(SubjectEntity.builder().build())
                .alternatives(Collections.emptyList())
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        assertThatThrownBy(() -> repository.save(question))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
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
                .subject(subject)
                .build();
    }

    private MultipleChoiceEntity getMultipleChoice() {
        return MultipleChoiceEntity.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .active(true)
                .subject(subject)
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