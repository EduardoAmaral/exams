package com.eamaral.exams.exam.application.dto;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import com.eamaral.exams.question.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamDTO implements Exam {

    private String title;

    private List<QuestionDTO> questions;

    private String author;

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }
}
