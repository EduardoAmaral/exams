package com.eamaral.worker.question.domain.service;

import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.domain.port.QuestionDocumentPort;
import com.eamaral.worker.question.domain.port.QuestionPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionPort questionPort;
    private final QuestionDocumentPort questionDocumentPort;

    public long upsert() {
        List<Question> questions = questionPort.findAll();

        questionDocumentPort.deleteAll();

        questionDocumentPort.save(questions);

        return questions.size();
    }
}
