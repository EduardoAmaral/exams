package com.eamaral.exams.question.application.dto;

import com.eamaral.exams.question.domain.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDTO implements Serializable, Comment {

    private Long id;

    @NotBlank(message = "{comment.message.required}")
    @Size(max = 300, message = "{comment.message.size}")
    private String message;

    @NotNull(message = "{comment.question.required}")
    private Long questionId;

    private String author;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime creationDate;

    public static List<CommentDTO> from(List<Comment> comments) {
        return CollectionUtils.emptyIfNull(comments)
                .stream()
                .map(CommentDTO::from)
                .collect(toList());
    }

    public static CommentDTO from(Comment comment) {
        return builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .message(comment.getMessage())
                .questionId(comment.getQuestionId())
                .creationDate(comment.getCreationDate())
                .build();
    }
}

