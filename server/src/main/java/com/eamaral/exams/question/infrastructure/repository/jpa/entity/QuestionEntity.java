package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TB_QUESTION")
@Where(clause = "deleted = false")
public abstract class QuestionEntity implements Question {

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
    @Setter
    private boolean deleted;

    @Column(nullable = false)
    private boolean shared;

    @Column
    @NotBlank(message = "{question.answer.required}")
    private String correctAnswer;

    @Column
    @Size(max = 255, message = "{question.keywords.size}")
    private String keywords;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectEntity subject;

    @Column
    @NotEmpty(message = "{question.author.required}")
    private String author;

}
