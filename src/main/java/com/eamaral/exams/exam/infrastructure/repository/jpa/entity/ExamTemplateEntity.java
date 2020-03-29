package com.eamaral.exams.exam.infrastructure.repository.jpa.entity;

import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.converter.QuestionConverter;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "TB_EXAM_TEMPLATE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Where(clause = "active = true")
public class ExamTemplateEntity implements ExamTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{exam.template.title.required}")
    @Column
    private String title;

    @NotBlank(message = "{exam.template.author.required}")
    @Column
    private String author;

    @NotEmpty(message = "{exam.template.questions.required}")
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestionEntity> questions;

    @Column(nullable = false)
    private boolean active;

    public static ExamTemplateEntity from(ExamTemplate examTemplate) {
        ExamTemplateEntityBuilder builder = builder();

        if (examTemplate != null) {
            builder.author(examTemplate.getAuthor())
                    .id(examTemplate.getId())
                    .active(true)
                    .title(examTemplate.getTitle());

            if (examTemplate.getQuestions() != null) {
                builder.questions(examTemplate.getQuestions()
                        .stream()
                        .map(QuestionConverter::from)
                        .collect(toList()));
            }
        }

        return builder.build();
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

}
