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
public class CommentEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "{comment.message.required}")
    @Size(max = 3000, message = "{comment.message.size}")
    private String message;

    @Column(name = "QUESTION_ID")
    @NotNull(message = "{comment.question.required}")
    private Long questionId;

    @Column
    @NotNull(message = "{comment.author.required}")
    private String authorId;

    @Column
    @NotNull(message = "{comment.creationDate.required}")
    private ZonedDateTime creationDate;

    public static CommentEntity from(Comment comment) {
        return builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .questionId(comment.getQuestionId())
                .authorId(comment.getAuthorId())
                .creationDate(ZonedDateTime.now())
                .build();
    }

    public Comment toComment() {
        return Comment.builder()
                .id(getId())
                .message(getMessage())
                .questionId(getQuestionId())
                .authorId(getAuthorId())
                .creationDate(getCreationDate())
                .build();
    }
}
