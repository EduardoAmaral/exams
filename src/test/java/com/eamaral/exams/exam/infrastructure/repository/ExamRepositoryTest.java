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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ExamRepositoryTest extends JpaIntegrationTest {

    private final String currentUser = "10001";
    @Autowired
    private ExamRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private List<QuestionEntity> questions;

    @Before
    public void setUp() {
        persistQuestions();
    }

    @Test
    public void save_whenFieldsAreValid_shouldReturnSuccess() {
        Exam exam = getExam();

        exam = repository.save(exam);

        assertThat(exam.getId()).isNotBlank();
    }

    @Test
    public void save_whenAuthorIsNotInformed_shouldThrowConstraintViolationException() {
        List<String> validationMessages = List.of("Exam's title is required",
                "Exam's questions are required",
                "Exam's author is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(ExamEntity.builder()
                        .questions(emptyList())
                        .build()))
                .matches(e -> e.getConstraintViolations().size() == 3)
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    public void findById_whenExamExistsAndCurrentUserIsItsAuthor_shouldReturnAnExam() {
        Exam exam = repository.save(getExam());

        Optional<Exam> result = repository.findById(exam.getId(), currentUser);

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .extracting("title", "author")
                .containsExactly("Exam 1", currentUser);
        assertThat(result.get().getQuestions()).hasSize(2);
    }

    @Test
    public void findById_whenExamExistsButAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        Exam exam = repository.save(getExam());

        Optional<Exam> result = repository.findById(exam.getId(), "user");

        assertThat(result).isEmpty();
    }

    @Test
    public void findById_whenExamDoesNotExist_shouldReturnEmpty() {
        Optional<Exam> result = repository.findById("1", "user");

        assertThat(result).isEmpty();
    }

    @Test
    public void findByUser_shouldReturnTheAuthorsExams() {
        repository.save(getExam());

        List<Exam> exams = repository.findByUser(currentUser);

        assertThat(exams).isNotEmpty();
    }

    @Test
    public void delete_shouldRemoveAnExam() {
        Exam exam = repository.save(getExam());

        assertThat(repository.findByUser(currentUser)).hasSize(1);

        repository.delete(exam);

        assertThat(repository.findByUser(currentUser)).hasSize(0);
    }

    private Exam getExam() {
        return ExamEntity.builder()
                .title("Exam 1")
                .author(currentUser)
                .questions(questions)
                .build();
    }

    private void persistQuestions() {
        SubjectEntity subject = SubjectEntity.from(subjectRepository.save(SubjectEntity.builder()
                .description("English")
                .build()));
        List<Question> questions = List.of(TrueOrFalseEntity.builder()
                        .statement("True or False question")
                        .type(QuestionType.TRUE_OR_FALSE)
                        .correctAnswer("True")
                        .active(true)
                        .sharable(true)
                        .subject(subject)
                        .topic("Idioms")
                        .author(currentUser)
                        .build(),
                MultipleChoiceEntity.builder()
                        .statement("Multiple choice question")
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .correctAnswer("Three")
                        .active(true)
                        .sharable(true)
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