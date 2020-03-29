package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamTemplateEntity;
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

public class ExamTemplateRepositoryTest extends JpaIntegrationTest {

    private final String currentUser = "10001";

    @Autowired
    private ExamTemplateRepository repository;

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
        ExamTemplate examTemplate = getExamTemplate();

        examTemplate = repository.save(examTemplate);

        assertThat(examTemplate.getId()).isNotBlank();
    }

    @Test
    public void save_whenAuthorIsNotInformed_shouldThrowConstraintViolationException() {
        List<String> validationMessages = List.of("Exam's title is required",
                "Exam's questions are required",
                "Exam's author is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(ExamTemplateEntity.builder()
                        .questions(emptyList())
                        .build()))
                .matches(e -> e.getConstraintViolations().size() == 3)
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    public void findById_whenExamTemplateExistsAndCurrentUserIsItsAuthor_shouldReturnIt() {
        ExamTemplate examTemplate = repository.save(getExamTemplate());

        Optional<ExamTemplate> result = repository.findById(examTemplate.getId(), currentUser);

        assertThat(result).isNotEmpty();
        assertThat(result.get())
                .extracting("title", "author")
                .containsExactly("Exam 1", currentUser);
        assertThat(result.get().getQuestions()).hasSize(2);
    }

    @Test
    public void findById_whenExamTemplateExistsButAuthorIsNotTheCurrentUser_shouldReturnEmpty() {
        ExamTemplate examTemplate = repository.save(getExamTemplate());

        Optional<ExamTemplate> result = repository.findById(examTemplate.getId(), "user");

        assertThat(result).isEmpty();
    }

    @Test
    public void findById_whenExamTemplateDoesNotExist_shouldReturnEmpty() {
        Optional<ExamTemplate> result = repository.findById("1", "user");

        assertThat(result).isEmpty();
    }

    @Test
    public void findByUser_shouldReturnTheAuthorsExamTemplates() {
        repository.save(getExamTemplate());

        List<ExamTemplate> examTemplates = repository.findByUser(currentUser);

        assertThat(examTemplates).isNotEmpty();
    }

    @Test
    public void delete_shouldRemoveAnExamTemplate() {
        ExamTemplate examTemplate = repository.save(getExamTemplate());

        assertThat(repository.findByUser(currentUser)).hasSize(1);

        repository.delete(examTemplate);

        assertThat(repository.findByUser(currentUser)).hasSize(0);
    }

    private ExamTemplate getExamTemplate() {
        return ExamTemplateEntity.builder()
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