package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
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

    private SubjectEntity english;
    private SubjectEntity portuguese;

    private String currentUser = "1";

    @Before
    public void setUp() {

        english = SubjectEntity.from(
                subjectRepository.save(
                        SubjectEntity.builder()
                                .description("English")
                                .build()));

        portuguese = SubjectEntity.from(
                subjectRepository.save(
                        SubjectEntity.builder()
                                .description("Portuguese")
                                .build()));
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

        String author = "1";
        List<Question> result = repository.findByUser(author);

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

        Optional<Question> result = repository.find(question.getId(), currentUser);

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

        Optional<Question> result = repository.find(question.getId(), currentUser);

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
        Long questionId = 1L;
        Optional<Question> result = repository.find(questionId, currentUser);

        assertThat(result).isEmpty();
    }

    @Test
    public void findById_whenIdExistsButIsNotSharableAndQuestionAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        Question question = getMultipleChoice();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId(), "ABC");

        assertThat(result).isEmpty();
    }

    @Test
    public void findById_whenTheAuthorIsNotTheCurrentUserButQuestionIsShared_shouldReturnAQuestion() {
        Question question = getTrueOrFalseQuestion();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId(), "ABC");

        assertThat(result).isNotEmpty();
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
                .subject(english)
                .author("1")
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
                        Question::getAuthor)
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
    public void findByCriteriaWithStatement_shouldReturnOnlyQuestionsThatMatchWithTheStatementFiltered() {
        repository.saveAll(getQuestions());

        String statementFiltered = "Can";
        Question question = TrueOrFalseEntity.builder()
                .statement(statementFiltered)
                .build();
        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .extracting(Question::getStatement)
                .allMatch(statement -> statement.contains(statementFiltered), "The result should only contain questions where their statement is like the filter");
    }

    @Test
    public void findByCriteriaWithType_shouldReturnOnlyQuestionsThatMatchWithTheTypeFiltered() {
        repository.saveAll(getQuestions());

        Question question = TrueOrFalseEntity.builder()
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .extracting(Question::getType)
                .allMatch(type -> type == QuestionType.MULTIPLE_CHOICES, "The result should only contain questions where their type matches the filter");
    }

    @Test
    public void findByCriteriaWithTopic_shouldReturnOnlyQuestionsThatMatchWithTheTopicFiltered() {
        repository.saveAll(getQuestions());

        String topicFiltered = "language";
        Question question = TrueOrFalseEntity.builder()
                .topic(topicFiltered)
                .build();

        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .extracting(Question::getTopic)
                .allMatch(topic -> topic.toLowerCase().contains(topicFiltered), "The result should only contain questions where their topic matches the filter");
    }

    @Test
    public void findByCriteriaWithCurrentUser_shouldReturnOnlyQuestionsCreatedByTheCurrentUserOrShared() {
        repository.saveAll(getQuestions());

        String currentUser = "20001";
        Question question = TrueOrFalseEntity.builder().build();

        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .hasSize(2)
                .allMatch(q -> q.getAuthor().equals(currentUser) || q.isSharable(), "The result should only contain questions created by the user or shared questions");
    }

    @Test
    public void findByCriteriaWithCurrentUserAsAuthor_shouldReturnOnlyQuestionsCreatedByTheCurrentUserOrShared() {
        repository.saveAll(getQuestions());

        String currentUser = "20001";
        Question question = TrueOrFalseEntity.builder()
                .author(currentUser)
                .build();

        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .hasSize(2)
                .allMatch(q -> q.getAuthor().equals(currentUser) || q.isSharable(), "The result should only contain questions created by the user or shared questions");
    }

    @Test
    public void findByCriteriaWithSubject_shouldReturnOnlyQuestionThatMatchTheSubjectFiltered() {
        repository.saveAll(getQuestions());

        Long subjectIdFiltered = 1L;

        Question question = TrueOrFalseEntity.builder()
                .subject(SubjectEntity.builder()
                        .id(subjectIdFiltered)
                        .build())
                .build();

        List<Question> result = repository.findByCriteria(question, "20001");

        assertThat(result).extracting(q -> q.getSubject().getId())
                .allMatch(id -> id.equals(subjectIdFiltered), "The result should only contain questions where their subject matches the filter");
    }

    @Test
    public void findByCriteria_whenDoesNotHaveAnyMatch_shouldReturnEmpty() {
        List<Question> result = repository.findByCriteria(TrueOrFalseEntity.builder().build(), "");

        assertThat(result).isEmpty();
    }

    @Test
    public void findByCriteriaWithAuthor_shouldReturnAllSharedQuestionsFromTheAuthor() {
        repository.saveAll(getQuestions());

        String author = "20001";

        Question criteria = TrueOrFalseEntity.builder()
                .author(author)
                .build();

        List<Question> questions = repository.findByCriteria(criteria, currentUser);

        assertThat(questions).extracting(Question::getAuthor)
                .allMatch(a -> a.equals(author), "The result should only contain questions where their author matches the filter and they are shared");
    }

    @Test
    public void delete_shouldRemoveAQuestion() {
        String author = "1";

        Question question = getMultipleChoice();
        question = repository.save(question);

        List<Question> questions = repository.findByUser(author);

        assertThat(questions).hasSize(1);

        repository.delete(question);

        questions = repository.findByUser(author);

        assertThat(questions).hasSize(0);
    }

    @Test
    public void save_whenFieldsAreInvalid_shouldThrowsException() {
        Question question = MultipleChoiceEntity.builder()
                .subject(english)
                .alternatives(Collections.emptyList())
                .correctAnswer("")
                .statement("")
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        List<String> validationMessages = List.of("Question's author id is required",
                "Question's correct answer is required",
                "Question's statement is required",
                "Question's statement should have between 4 and 2000 characters",
                "Question's alternatives are required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(question))
                .matches(e -> e.getConstraintViolations().size() == 5)
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
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
                        .topic("Language; Latin Language")
                        .subject(portuguese)
                        .author("20001")
                        .build());
    }

    private TrueOrFalseEntity getTrueOrFalseQuestion() {
        return TrueOrFalseEntity.builder()
                .statement("Can I test TF?")
                .type(QuestionType.TRUE_OR_FALSE)
                .correctAnswer("True")
                .sharable(true)
                .subject(english)
                .topic("Language")
                .author(currentUser)
                .build();
    }

    private MultipleChoiceEntity getMultipleChoice() {
        return MultipleChoiceEntity.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .sharable(false)
                .subject(english)
                .topic("Test")
                .alternatives(getAlternatives())
                .author(currentUser)
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