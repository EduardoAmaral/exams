package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTION_COMMENT")
public class CommentEntity implements Comment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{comment.message.required}")
    @Size(max = 300, message = "{comment.message.size}")
    private String message;

    @NotNull(message = "{comment.question.required}")
    private Long questionId;

    @NotNull(message = "{comment.author.required}")
    private String author;

    @NotNull(message = "{comment.creationDate.required}")
    private ZonedDateTime creationDate;

    public static CommentEntity from(Comment comment) {
        return builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .questionId(comment.getQuestionId())
                .author(comment.getAuthor())
                .creationDate(ZonedDateTime.now())
                .build();
    }
}