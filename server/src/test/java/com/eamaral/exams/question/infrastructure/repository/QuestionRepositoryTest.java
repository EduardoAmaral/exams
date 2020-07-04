package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.MultipleChoiceEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.SubjectEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.TrueOrFalseEntity;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class QuestionRepositoryTest extends JpaIntegrationTest {

    private final String currentUser = "1";

    private SubjectEntity english;
    private SubjectEntity portuguese;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionRepository repository;

    @BeforeEach
    void setUp() {
        insertSubjects();
    }

    @Test
    @DisplayName("should save a question")
    void save_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Question question = getTrueOrFalseQuestion();

        Question result = repository.save(question);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    @DisplayName("should validate required fields when saving a question")
    void save_whenFieldsAreInvalid_shouldThrowsException() {
        Question question = MultipleChoiceEntity.builder()
                .subject(english)
                .alternatives(Collections.emptyList())
                .correctAnswer("")
                .statement("")
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        List<String> validationMessages = List.of("Author id is required",
                "Correct answer is required",
                "Statement is required",
                "Statement should have between 4 and 2000 characters",
                "Alternatives are required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(question))
                .matches(e -> e.getConstraintViolations().size() == 5)
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should validate required subject when saving a question")
    void save_whenSubjectIsBlank_shouldThrowsException() {
        Question question = MultipleChoiceEntity.builder()
                .subject(SubjectEntity.builder().build())
                .alternatives(Collections.emptyList())
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        assertThatThrownBy(() -> repository.save(question))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    @DisplayName("should save a batch of questions")
    void saveAll_whenFieldsAreValid_shouldReturnQuestionsWithIds() {
        List<Question> questions = getQuestions();

        List<Question> result = repository.saveAll(questions);

        assertThat(result)
                .extracting(Question::getId)
                .isNotNull();
    }

    @Test
    @DisplayName("should retrieve all questions by author")
    void findByUser_shouldOnlyReturnQuestionsCreatedByTheUser() {
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
    @DisplayName("should find a TRUE OR FALSE question when search an existent question by id")
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
    @DisplayName("should find a MULTIPLE CHOICES question when search an existent question by id")
    void findById_whenIdExistsAndQuestionIsMultipleChoice_shouldReturnAMultipleChoiceQuestion() {
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
    @DisplayName("should return empty when getting a nonexistent question by id")
    void findById_whenIdDoesNotExist_shouldReturnEmpty() {
        Long questionId = 1L;
        Optional<Question> result = repository.find(questionId, currentUser);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return empty when getting a question that is not shared and was created by other user")
    void findById_whenIdExistsButIsNotSharableAndQuestionAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        Question question = getMultipleChoice();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId(), "ABC");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return a question that is shared when it exists")
    void findById_whenTheAuthorIsNotTheCurrentUserButQuestionIsShared_shouldReturnAQuestion() {
        Question question = getTrueOrFalseQuestion();

        question = repository.save(question);

        Optional<Question> result = repository.find(question.getId(), "ABC");

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("should update a question")
    void update_shouldUpdateTheFieldsOfAQuestion() {
        Question question = getMultipleChoice();
        question = repository.save(question);

        MultipleChoiceEntity entity = MultipleChoiceEntity.builder()
                .id(question.getId())
                .alternatives(AlternativeEntity.from(question.getAlternatives()))
                .type(question.getType())
                .correctAnswer("World")
                .statement("Hello")
                .shared(false)
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
                        Question::isShared,
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
    @DisplayName("should remove a question")
    void delete_shouldRemoveAQuestion() {
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
    @DisplayName("should retrieve only author's questions or shared ones when searching by criteria")
    void findByCriteriaWithCurrentUser_shouldReturnOnlyQuestionsCreatedByTheCurrentUserOrShared() {
        repository.saveAll(getQuestions());

        String currentUser = "20001";
        Question question = TrueOrFalseEntity.builder().build();

        List<Question> result = repository.findByCriteria(question, currentUser);

        assertThat(result)
                .hasSize(2)
                .allMatch(q -> q.getAuthor().equals(currentUser) || q.isShared(), "The result should only contain questions created by the user or shared questions");
    }

    @Test
    @DisplayName("should return empty if any question matches the criteria used")
    void findByCriteria_whenDoesNotHaveAnyMatch_shouldReturnEmpty() {
        List<Question> result = repository.findByCriteria(TrueOrFalseEntity.builder().build(), "");

        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("findByCriteriaScenarios")
    @DisplayName("should retrieve all questions by criteria")
    void findByCriteriaWithStatement_shouldReturnOnlyQuestionsThatMatchWithTheStatementFiltered(SearchByCriteriaScenario scenario) {
        repository.saveAll(getQuestions());

        List<Question> result = repository.findByCriteria(scenario.criteria, currentUser);

        assertThat(result)
                .extracting(scenario.assertField)
                .allMatch(content -> scenario.matcher.test(content.toString()));
    }

    private static Stream<SearchByCriteriaScenario> findByCriteriaScenarios() {
        return Stream.of(
                SearchByCriteriaScenario.builder()
                        .assertField("statement")
                        .criteria(
                                TrueOrFalseEntity.builder()
                                        .statement("Can")
                                        .build()
                        )
                        .matcher(statement -> statement.contains("Can"))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("type")
                        .criteria(
                                TrueOrFalseEntity.builder()
                                        .type(QuestionType.MULTIPLE_CHOICES)
                                        .build()
                        )
                        .matcher(type -> type.equals(QuestionType.MULTIPLE_CHOICES.toString()))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("topic")
                        .criteria(
                                TrueOrFalseEntity.builder()
                                        .topic("language")
                                        .build()
                        )
                        .matcher(topic -> topic.contains("language"))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("author")
                        .criteria(
                                TrueOrFalseEntity.builder()
                                        .author("20001")
                                        .build()
                        )
                        .matcher(author -> author.equals("20001"))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("subject.id")
                        .criteria(
                                TrueOrFalseEntity.builder()
                                        .subject(SubjectEntity.builder()
                                                .id(1L)
                                                .build())
                                        .build()
                        )
                        .matcher(subject -> subject.equals("1"))
                        .build()
        );
    }

    @Builder
    private static class SearchByCriteriaScenario {
        private final Question criteria;
        private final String assertField;
        private final Predicate<String> matcher;

        @Override
        public String toString() {
            return String.format("should retrieve all questions where %s matches %s", assertField, criteria);
        }
    }

    private void insertSubjects() {
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
                .shared(true)
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
                .shared(false)
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