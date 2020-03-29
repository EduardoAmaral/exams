package com.eamaral.exams.exam.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.infrastructure.repository.jpa.entity.ExamEntity;
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

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ExamRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private ExamRepository repository;

    @Autowired
    private ExamTemplateRepository examTemplateRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private final String currentUser = "10001";

    private ExamTemplateEntity template;

    @Before
    public void setUp() {
        List<QuestionEntity> questions = persistQuestions();
        persistTemplate(questions);
    }

    @Test
    public void create_shouldSaveAnExam() {
        Exam exam = getExam();
        exam = repository.save(exam);
        assertThat(exam.getId()).isNotBlank();
    }

    private Exam getExam() {
        return ExamEntity.builder()
                .template(template)
                .startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MAX)
                .mockTest(false)
                .build();
    }

    private void persistTemplate(List<QuestionEntity> questions) {
        ExamTemplateEntity entity = ExamTemplateEntity.builder()
                .title("Exam 1")
                .author(currentUser)
                .questions(questions)
                .build();

        this.template = ExamTemplateEntity.from(examTemplateRepository.save(entity));
    }

    private List<QuestionEntity> persistQuestions() {
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

        return questionRepository.saveAll(questions)
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