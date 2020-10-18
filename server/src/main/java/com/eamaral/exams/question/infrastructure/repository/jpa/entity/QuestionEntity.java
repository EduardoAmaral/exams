package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import com.eamaral.exams.question.domain.Question;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTION")
@Where(clause = "deleted = false")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(2000)")
    @NotBlank(message = "{question.statement.required}")
    @Size(max = 2000, message = "{question.statement.size}")
    private String statement;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{question.type.required}")
    private QuestionType type;

    @Column(columnDefinition = "varchar(3000)")
    @Size(max = 3000, message = "{question.solution.size}")
    private String solution;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "CORRECT_ANSWER")
    @NotBlank(message = "{question.answer.required}")
    private String correctAnswer;

    @Column
    @Size(max = 255, message = "{question.keywords.size}")
    private String keywords;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID")
    private SubjectEntity subject;

    @Column(name = "AUTHOR_ID")
    @NotEmpty(message = "{question.author.required}")
    private String authorId;

    @OneToMany
    @JoinColumn(name = "QUESTION_ID", insertable = false, updatable = false)
    private List<AlternativeEntity> alternatives;

    public static QuestionEntity from(Question question) {
        return QuestionEntity.builder()
                .id(question.getId())
                .correctAnswer(question.getCorrectAnswer())
                .solution(question.getSolution())
                .statement(question.getStatement())
                .type(question.getType())
                .keywords(question.getKeywords())
                .subject(SubjectEntity.from(question.getSubject()))
                .authorId(question.getAuthorId())
                .alternatives(AlternativeEntity.from(question))
                .build();
    }

    public Question toDomain() {
        return Question.builder()
                .id(id)
                .statement(statement)
                .subject(subject.toDomain())
                .correctAnswer(correctAnswer)
                .authorId(authorId)
                .keywords(keywords)
                .type(type)
                .solution(solution)
                .alternatives(getAlternativesBasedOnType())
                .build();
    }

    private List<Alternative> getAlternativesBasedOnType() {
        if (QuestionType.TRUE_OR_FALSE.equals(type)) {
            return List.of(AlternativeEntity.builder()
                            .description("True")
                            .build(),
                    AlternativeEntity.builder()
                            .description("False")
                            .build());
        }

        return new ArrayList<>(emptyIfNull(getAlternatives()));
    }
}
