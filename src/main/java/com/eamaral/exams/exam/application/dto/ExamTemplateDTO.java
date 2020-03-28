package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.ExamTemplate;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamTemplateDTO implements ExamTemplate {

    private String id;

    @NotBlank(message = "{exam.template.title.required}")
    private String title;

    @NotEmpty(message = "{exam.template.questions.required}")
    private List<QuestionDTO> questions;

    private String author;

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public static ExamTemplateDTO from(ExamTemplate examTemplate) {
        final ExamTemplateDTOBuilder builder = builder();

        if (examTemplate != null) {
            builder.id(examTemplate.getId())
                    .title(examTemplate.getTitle())
                    .author(examTemplate.getAuthor())
                    .questions(QuestionDTO.from(examTemplate.getQuestions()));
        }

        return builder.build();
    }
}
