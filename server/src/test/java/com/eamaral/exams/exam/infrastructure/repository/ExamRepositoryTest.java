package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.QuestionRepository;
import com.eamaral.exams.question.infrastructure.repository.SubjectRepository;
import com.eamaral.exams.question.infrastructure.repository.converter.QuestionConverter;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
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

    private List<QuestionEntity> questions;

    @BeforeEach
    void setUp() {
        persistQuestions();
    }

    @Test
    @DisplayName("save should save an exam when required fields are filled")
    void save_shouldSaveAnExam() {
        Exam exam = getExam().build();
        exam = repository.save(exam);
        assertThat(exam.getId()).isNotZero();
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
                .matches(e -> e.getConstraintViolations().size() == 3)
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("findByUser should retrieve all exams created by a given user")
    void findByUser_shouldReturnAllExamsCreatedByTheUser() {
        repository.save(getExam().build());
        List<Exam> exams = repository.findByUser(currentUser);

        assertThat(exams).hasSize(1);
    }

    @Test
    @DisplayName("findByUser should return empty if a given user doesn't have exams registred")
    void findByUser_shouldReturnEmpty() {
        repository.save(getExam().build());
        List<Exam> exams = repository.findByUser("000009");

        assertThat(exams).hasSize(0);
    }

    @Test
    @DisplayName("findAvailable should retrieve all exams where current time is between exams start and end date/time")
    void findAvailable_shouldReturnAllExamsWhereTheCurrentDateIsBetweenTheirInterval() {
        LocalDateTime today = LocalDateTime.now();
        repository.save(getExam()
                .startDateTime(today.minusMinutes(30))
                .endDateTime(today.plusHours(2))
                .mockTest(false)
                .build());

        LocalDateTime nextDay = today.plusDays(1);
        repository.save(getExam()
                .startDateTime(nextDay)
                .endDateTime(nextDay.plusHours(2))
                .mockTest(false)
                .build());

        List<Exam> exams = repository.findAvailable();

        assertThat(exams).hasSize(1);
    }

    @Test
    @DisplayName("findAvailable should retrieve all mock tests where current time is between the start and end date/time")
    void findAvailable_shouldRetrieveAllMockTests() {
        repository.save(getExam()
                .startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MIN)
                .mockTest(true)
                .build());

        LocalDateTime nextDay = LocalDateTime.now().plusDays(1);
        repository.save(getExam()
                .startDateTime(nextDay)
                .endDateTime(nextDay.plusHours(2))
                .mockTest(false)
                .build());

        List<Exam> exams = repository.findAvailable();

        assertThat(exams).hasSize(1);
    }

    @Test
    @DisplayName("findById should return the exam when it exists and the current user is its author")
    void findById_whenExamExistsAndCurrentUserIsItsAuthor_shouldReturnIt() {
        Exam exam = repository.save(getExam().build());

        Optional<Exam> result = repository.findById(exam.getId(), currentUser);

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .extracting("title", "author")
                .containsExactly("Exam 1", currentUser);
        assertThat(result.get().getQuestions()).hasSize(2);
    }

    @Test
    @DisplayName("findById should return empty if current user is not the exam's author")
    void findById_whenExamExistsButAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        Exam exam = repository.save(getExam().build());

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
        Exam exam = repository.save(getExam().build());

        assertThat(repository.findByUser(currentUser)).hasSize(1);

        repository.delete(exam);

        assertThat(repository.findByUser(currentUser)).hasSize(0);
    }

    private ExamEntity.ExamEntityBuilder getExam() {
        return ExamEntity.builder()
                .title("Exam 1")
                .author(currentUser)
                .questions(questions)
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .mockTest(false);
    }

    private void persistQuestions() {
        SubjectEntity subject = SubjectEntity.from(subjectRepository.save(SubjectEntity.builder()
                .description("English")
                .build()));
        List<Question> questions = List.of(TrueOrFalseEntity.builder()
                        .statement("True or False question")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .correctAnswer("True")
                        .shared(true)
                        .subject(subject)
                        .topic("Idioms")
                        .author(currentUser)
                        .build(),
                MultipleChoiceEntity.builder()
                        .statement("Multiple choice question")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("Three")
                        .shared(true)
                        .subject(subject)
                        .topic("Idioms")
                        .alternatives(getAlternatives())
                        .author(currentUser)
                        .build());

        this.questions = questionRepository.saveAll(questions)
                .stream()
                .map(QuestionConverter::from)
                .collect(toList());
    }

    private List<AlternativeEntity> getAlternatives() {
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