package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.MultipleChoiceEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.SubjectEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.TrueOrFalseEntity;
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
    public void findByUser_shouldOnlyReturnQuestionsCreatedByTheUser() {
        List<Question> questions = getQuestions();

        repository.saveAll(questions);

        String userId = "1";
        List<Question> result = repository.findByUser(userId);

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
        long questionId = 1L;
        Optional<Question> result = repository.find(questionId);

        assertThat(result).isEmpty();
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
                .userId("1")
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
                        q -> q.getSubject().getDescription(),
                        Question::getUserId)
                .containsExactlyInAnyOrder(
                        entity.getId(),
                        "Hello",
                        "Hello World!",
                        "World",
                        "Greetings",
                        false,
                        "English",
                        "1");
    }

    @Test
    public void findByFilterWithStatement_shouldReturnOnlyQuestionsThatMatchWithTheStatementFiltered() {
        repository.saveAll(getQuestions());

        String statementFiltered = "Can";
        Question question = TrueOrFalseEntity.builder()
                .userId("1")
                .statement(statementFiltered)
                .build();
        List<Question> result = repository.findByFilter(question);

        assertThat(result)
                .extracting(Question::getStatement)
                .allMatch(statement -> statement.contains(statementFiltered), "The result should contain only questions where the statement is like the filter");
    }

    @Test
    public void findByFilterWithType_shouldReturnOnlyQuestionsThatMatchWithTheTypeFiltered() {
        repository.saveAll(getQuestions());

        Question question = TrueOrFalseEntity.builder()
                .userId("1")
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        List<Question> result = repository.findByFilter(question);

        assertThat(result)
                .extracting(Question::getType)
                .allMatch(type -> type == QuestionType.MULTIPLE_CHOICES, "The result should contain only questions where the type matches the filter");
    }

    @Test
    public void findByFilterWithTopic_shouldReturnOnlyQuestionsThatMatchWithTheTopicFiltered() {
        repository.saveAll(getQuestions());

        String topicFiltered = "language";
        Question question = TrueOrFalseEntity.builder()
                .userId("1")
                .topic(topicFiltered)
                .build();

        List<Question> result = repository.findByFilter(question);

        assertThat(result)
                .extracting(Question::getTopic)
                .allMatch(topic -> topic.toLowerCase().contains(topicFiltered), "The result should contain only questions where the topic matches the filter");
    }

    @Test
    public void findByFilter_whenDoesNotHaveAnyMatch_shouldReturnEmpty() {
        List<Question> result = repository.findByFilter(TrueOrFalseEntity.builder().build());

        assertThat(result).isEmpty();
    }

    @Test
    public void delete_shouldRemoveAQuestion() {
        String userId = "1";

        Question question = getMultipleChoice();
        question = repository.save(question);

        List<Question> questions = repository.findByUser(userId);

        assertThat(questions).hasSize(1);

        repository.delete(question);

        questions = repository.findByUser(userId);

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
                getMultipleChoice(),
                TrueOrFalseEntity.builder()
                        .statement("Can I test TF?")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .correctAnswer("True")
                        .active(true)
                        .subject(subject)
                        .userId("20001")
                        .build());
    }

    private TrueOrFalseEntity getTrueOrFalseQuestion() {
        return TrueOrFalseEntity.builder()
                .statement("Can I test TF?")
                .type(QuestionType.TRUE_OR_FALSE)
                .correctAnswer("True")
                .active(true)
                .subject(subject)
                .topic("Language")
                .userId("1")
                .build();
    }

    private MultipleChoiceEntity getMultipleChoice() {
        return MultipleChoiceEntity.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .active(true)
                .subject(subject)
                .topic("Test")
                .alternatives(getAlternatives())
                .userId("1")
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