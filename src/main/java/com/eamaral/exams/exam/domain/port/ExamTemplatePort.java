package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.ExamTemplate;

import java.util.List;

public interface ExamTemplatePort {

    void save(ExamTemplate examTemplate);

    List<ExamTemplate> findByUser(String currentUser);

    ExamTemplate findById(Long id, String currentUser);

    void delete(Long id, String currentUser);
}
