package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.infrastructure.repository.jpa.ExamJpaRepository;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.infrastructure.repository.QuestionRepository;
import com.eamaral.exams.question.infrastructure.repository.SubjectRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ExamRepositoryTest extends JpaIntegrationTest {

    private final String currentUser = "10001";

    @Autowired
    private ExamRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamJpaRepository examJpaRepository;

    private List<Question> questions;

    @BeforeEach
    void setUp() {
        persistQuestions();
    }

    @Test
    @DisplayName("save should save an exam when required fields are filled")
    void save_shouldSaveAnExam() {
        Exam exam = getExam().build();
        repository.save(exam);

        final ExamEntity examEntity = examJpaRepository.findAll().get(0);
        assertThat(examEntity.getId()).isNotZero();
        assertThat(examEntity.isMockTest()).isFalse();
        assertThat(examEntity.getQuestions())
                .extracting(Question::getStatement)
                .containsExactlyInAnyOrder("True or False question", "Multiple choice question");
    }

    @Test
    @DisplayName("save should throw constraint validation exception when save without required fields")
    void save_shouldThrowConstraintViolationException() {
        List<String> validationMessages = List.of("Title is required",
                "Questions are required",
                "Author is required");

        final ExamEntity emptyExam = ExamEntity.builder().build();
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(emptyExam))
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should create a mock test without dates")
    void save_mockTest() {
        Exam exam = getExam()
                .mockTest(true)
                .startDateTime(null)
                .endDateTime(null)
                .build();

        repository.save(exam);

        final ExamEntity examEntity = examJpaRepository.findAll().get(0);
        assertThat(examEntity.getId()).isNotZero();
        assertThat(examEntity.isMockTest()).isTrue();
        assertThat(examEntity.getQuestions())
                .extracting(Question::getStatement)
                .containsExactlyInAnyOrder("True or False question", "Multiple choice question");

    }

    @Test
    @DisplayName("findByUser should retrieve all exams created by a given user")
    void findByUser_shouldReturnAllExamsCreatedByTheUser() {
        examJpaRepository.saveAndFlush(getExam().build());
        List<Exam> exams = repository.findByUser(currentUser);

        assertThat(exams).hasSize(1);
    }

    @Test
    @DisplayName("findByUser should return empty if a given user doesn't have exams registered")
    void findByUser_shouldReturnEmpty() {
        examJpaRepository.saveAndFlush(getExam().build());
        List<Exam> exams = repository.findByUser("000009");

        assertThat(exams).isEmpty();
    }

    @Test
    @DisplayName("findAvailable should retrieve all exams where current time is between exams start and end date/time")
    void findAvailable_shouldReturnAllExamsWhereTheCurrentDateIsBetweenTheirInterval() {
        ZonedDateTime today = ZonedDateTime.now();
        examJpaRepository.saveAndFlush(getExam()
                .startDateTime(today.minusMinutes(30))
                .endDateTime(today.plusHours(2))
                .build());

        ZonedDateTime nextDay = today.plusDays(1);
        examJpaRepository.saveAndFlush(getExam()
                .startDateTime(nextDay)
                .endDateTime(nextDay.plusHours(2))
                .build());

        List<Exam> exams = repository.findAvailable();

        assertThat(exams).hasSize(1);
    }

    @Test
    @DisplayName("findById should return the exam when it exists and the current user is its author")
    void findById_whenExamExistsAndCurrentUserIsItsAuthor_shouldReturnIt() {
        final ExamEntity exam = examJpaRepository.saveAndFlush(getExam().build());

        Optional<Exam> result = repository.findById(exam.getId(), currentUser);

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .extracting(Exam::getTitle, Exam::getAuthorId)
                .containsExactly("Exam 1", currentUser);
        assertThat(result.get().getQuestions()).hasSize(2);
    }

    @Test
    @DisplayName("findById should return empty if current user is not the exam's author")
    void findById_whenExamExistsButAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        final ExamEntity exam = examJpaRepository.saveAndFlush(getExam().build());

        Optional<Exam> result = repository.findById(exam.getId(), "user");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findById should return empty if exam id doesn't exist")
    void findById_whenExamDoesNotExist_shouldReturnEmpty() {
        Optional<Exam> result = repository.findById(1L, "user");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("delete should remove an exam")
    void delete_shouldRemoveAnExam() {
        final ExamEntity exam = examJpaRepository.saveAndFlush(getExam().build());

        assertThat(repository.findByUser(currentUser)).isNotEmpty();

        repository.delete(exam);

        assertThat(repository.findByUser(currentUser)).isEmpty();
    }

    private ExamEntity.ExamEntityBuilder getExam() {
        return ExamEntity.builder()
                .title("Exam 1")
                .authorId(currentUser)
                .questions(questions.stream().map(QuestionEntity::from).collect(toList()))
                .startDateTime(ZonedDateTime.now())
                .endDateTime(ZonedDateTime.now().plusHours(2));
    }

    private void persistQuestions() {
        Subject subject = subjectRepository.save(Subject.builder()
                .description("English")
                .build());
        List<Question> questions = List.of(Question.builder()
                        .statement("True or False question")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .correctAnswer("True")
                        .subject(subject)
                        .keywords("Idioms")
                        .authorId(currentUser)
                        .build(),
                Question.builder()
                        .statement("Multiple choice question")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("Three")
                        .subject(subject)
                        .keywords("Idioms")
                        .alternatives(getAlternatives())
                        .authorId(currentUser)
                        .build());

        questions.forEach(question -> questionRepository.save(question));
        this.questions = questionRepository.findByUser(currentUser);

    }

    private List<Alternative> getAlternatives() {
        return List.of(
                AlternativeEntity.builder()
                        .description("One")
                        .build(),
                AlternativeEntity.builder()
                        .description("Two")
                        .build(),
                AlternativeEntity.builder()
                        .description("Three")
                        .build(),
                AlternativeEntity.builder()
                        .description("Four")
                        .build(),
                AlternativeEntity.builder()
                        .description("Five")
                        .build());
    }
}