package com.eamaral.worker.question.infrastructure.repository.jpa.entity;

import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.domain.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTION")
@Where(clause = "deleted = false")
public class QuestionEntity {

    @Id
    @Column
    private Long id;

    @Column
    private String statement;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(nullable = false)
    private boolean deleted;

    @Column
    private String keywords;

    @Formula("SELECT S.DESCRIPTION FROM TB_SUBJECT S WHERE S.ID = SUBJECT_ID")
    private String subject;

    @Formula("SELECT CONCAT(U.NAME, ' ', U.SURNAME) FROM TB_USER U WHERE U.ID = AUTHOR_ID")
    private String author;

    public Question toDomain() {
        return Question.builder()
                .id(id)
                .statement(statement)
                .subject(subject)
                .keywords(keywords)
                .type(type)
                .author(author)
                .build();
    }
}
