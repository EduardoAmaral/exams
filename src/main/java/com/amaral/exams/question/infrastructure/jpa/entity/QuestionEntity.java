package com.amaral.exams.question.infrastructure.jpa.entity;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Where(clause = "active = true")
public abstract class QuestionEntity implements Question {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "varchar(2000)")
    @NotBlank(message = "{question.statement.required}")
    @Size(min = 4, max = 2000, message = "{question.statement.size}")
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
    private boolean active = true;

    @Column(nullable = false)
    private boolean sharable = false;

    @Column
    @NotBlank(message = "{question.answer.required}")
    private String correctAnswer;

    @Column
    private String topic;

}
