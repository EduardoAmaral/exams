package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.domain.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDTO implements Serializable, Comment {

    private Long id;

    @NotBlank(message = "{comment.message.required}")
    @Size(max = 300, message = "{comment.message.size}")
    private String message;

    @NotNull(message = "{comment.question.required}")
    private Long questionId;

    private String author;

}

