package com.eamaral.worker.question.domain.port;

import com.eamaral.worker.question.domain.Question;

import java.util.List;

public interface QuestionDocumentPort {
    void deleteAll();

    void save(List<Question> questions);
}
