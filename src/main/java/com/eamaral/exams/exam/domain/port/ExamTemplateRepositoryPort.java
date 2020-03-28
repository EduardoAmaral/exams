package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.ExamTemplate;

import java.util.List;
import java.util.Optional;

public interface ExamTemplateRepositoryPort {

    ExamTemplate save(ExamTemplate examTemplate);

    Optional<ExamTemplate> findById(String id, String currentUser);

    List<ExamTemplate> findByUser(String currentUser);

    void delete(ExamTemplate examTemplate);
}
