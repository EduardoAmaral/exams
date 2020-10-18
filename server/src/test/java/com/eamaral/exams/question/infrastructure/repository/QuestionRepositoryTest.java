package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class QuestionRepositoryTest extends JpaIntegrationTest {

    private final String currentUser = "1";

    private Subject english;
    private Subject portuguese;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private EntityManager entityManager;

    private static Stream<SearchByCriteriaScenario> findByCriteriaScenarios() {
        return Stream.of(
                SearchByCriteriaScenario.builder()
                        .assertField("statement")
                        .criteria(
                                Question.builder()
                                        .statement("Can")
                                        .build()
                        )
                        .matcher(statement -> statement.contains("Can"))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("type")
                        .criteria(
                                Question.builder()
                                        .type(QuestionType.MULTIPLE_CHOICES)
                                        .build()
                        )
                        .matcher(type -> type.equals(QuestionType.MULTIPLE_CHOICES.toString()))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("keywords")
                        .criteria(
                                Question.builder()
                                        .keywords("language")
                                        .build()
                        )
                        .matcher(keywords -> keywords.toLowerCase().contains("language"))
                        .build(),
                SearchByCriteriaScenario.builder()
                        .assertField("authorId")
                        .criteria(
                                Question.builder()
                                        .authorId("20001")
                                        .build()
                        )
                        .matcher(author -> author.equals("20001"))
                        .build()
        );
    }

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
    @DisplayName("Save multiple choices question should save question and its alternatives")
    void save_multipleChoices() {
        Question question = getMultipleChoice();

        Question result = repository.save(question);

        assertThat(entityManager.find(QuestionEntity.class, result.getId())).isNotNull();

        final Query query = entityManager.createQuery("SELECT a FROM AlternativeEntity a");
        assertThat(query.getResultList()).isNotEmpty();

        final List<Question> questions = repository.findByUser(currentUser);

        assertThat(questions.get(0).getAlternatives()).isNotEmpty();
    }

    @Test
    @DisplayName("should validate required fields when saving a question")
    void save_whenFieldsAreInvalid_shouldThrowsException() {
        Question question = Question.builder()
                .subject(english)
                .alternatives(Collections.emptyList())
                .correctAnswer("")
                .statement("")
                .type(QuestionType.MULTIPLE_CHOICES)
                .build();

        List<String> validationMessages = List.of("Author id is required",
                "Correct answer is required",
                "Statement is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(question))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should validate maximum value of fields")
    void save_whenStatementIsGreaterThanMaximum() {
        Question question = Question.builder()
                .subject(english)
                .correctAnswer("A")
                .authorId("1")
                .keywords(new String(new char[256]).replace('\0', 'A'))
                .statement(new String(new char[2001]).replace('\0', 'A'))
                .type(QuestionType.TRUE_OR_FALSE)
                .solution(new String(new char[3001]).replace('\0', 'A'))
                .build();

        List<String> validationMessages = List.of(
                "Statement should have a maximum of 2000 characters",
                "Solution should have a maximum of 3000 characters",
                "Keywords should have a maximum of 255 characters");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(question))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should validate required subject when saving a question")
    void save_whenSubjectIsBlank_shouldThrowsException() {
        Question question = Question.builder()
                .subject(Subject.builder().build())
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
    void findById_whenIdExistsAndQuestionIsTrueOrFalse_shouldReturnATrueOrFalseQuestion() {
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
        Question question = repository.save(getMultipleChoice());

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
    @DisplayName("should update a question")
    void update_shouldUpdateTheFieldsOfAQuestion() {
        Question question = getMultipleChoice();
        question = repository.save(question);

        Question entity = Question.builder()
                .id(question.getId())
                .alternatives(question.getAlternatives())
                .type(question.getType())
                .correctAnswer("World")
                .statement("Hello")
                .solution("Hello World!")
                .keywords("Greetings")
                .subject(english)
                .authorId("1")
                .build();

        question = repository.save(entity);

        assertThat(question)
                .extracting(
                        Question::getId,
                        Question::getStatement,
                        Question::getSolution,
                        Question::getCorrectAnswer,
                        Question::getKeywords,
                        q -> q.getSubject().getDescription(),
                        Question::getAuthorId)
                .containsExactlyInAnyOrder(
                        entity.getId(),
                        "Hello",
                        "Hello World!",
                        "World",
                        "Greetings",
                        "English",
                        "1");
    }

    @Test
    @DisplayName("should remove a question")
    void delete_shouldRemoveAQuestion() {
        entityManager.persist(QuestionEntity.from(getTrueOrFalseQuestion()));
        final List<Question> questions = repository.findByUser(currentUser);
        assertThat(questions).hasSize(1);

        repository.delete(questions.get(0));

        assertThat(repository.findByUser(currentUser)).isEmpty();
    }

    @Test
    @DisplayName("should return empty if any question matches the criteria used")
    void findByCriteria_whenDoesNotHaveAnyMatch_shouldReturnEmpty() {
        List<Question> result = repository.findByCriteria(Question.builder().build(), "");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return only questions that matches the subject filtered when search by criteria")
    void findByCriteriaWithSubject_shouldReturnOnlyQuestionThatMatchTheSubjectFiltered() {
        repository.saveAll(getQuestions());

        Question question = Question.builder()
                .subject(Subject.builder()
                        .id(english.getId())
                        .build())
                .build();

        List<Question> result = repository.findByCriteria(question, "20001");

        assertThat(result).extracting(q -> q.getSubject().getId())
                .isNotEmpty()
                .allMatch(id -> id.equals(english.getId()), "The result should only contain questions where their subject matches the filter");
    }

    @ParameterizedTest
    @MethodSource("findByCriteriaScenarios")
    @DisplayName("should retrieve all questions by criteria")
    void findByCriteria_shouldReturnOnlyQuestionsThatMatchesIt(SearchByCriteriaScenario scenario) {
        repository.saveAll(getQuestions());

        List<Question> result = repository.findByCriteria(scenario.criteria, currentUser);

        assertThat(result)
                .extracting(scenario.assertField)
                .isNotEmpty()
                .allMatch(content -> scenario.matcher.test(content.toString()));
    }

    private void insertSubjects() {
        english = subjectRepository.save(
                Subject.builder()
                        .description("English")
                        .build());

        portuguese = subjectRepository.save(
                Subject.builder()
                        .description("Portuguese")
                        .build());
    }

    private List<Question> getQuestions() {
        return List.of(
                getTrueOrFalseQuestion(),
                getMultipleChoice(),
                Question.builder()
                        .statement("Can I test TF?")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .correctAnswer("True")
                        .keywords("Language; Latin Language")
                        .subject(portuguese)
                        .authorId("20001")
                        .build());
    }

    private Question getTrueOrFalseQuestion() {
        return Question.builder()
                .statement("Can I test TF?")
                .type(QuestionType.TRUE_OR_FALSE)
                .correctAnswer("True")
                .subject(english)
                .keywords("Language")
                .authorId(currentUser)
                .build();
    }

    private Question getMultipleChoice() {
        return Question.builder()
                .statement("Can I test MC?")
                .type(QuestionType.MULTIPLE_CHOICES)
                .correctAnswer("B")
                .subject(english)
                .keywords("Test")
                .alternatives(getAlternatives())
                .authorId(currentUser)
                .build();
    }

    private List<Alternative> getAlternatives() {
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

}