package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Subject;

import java.util.List;

public interface SubjectPort {

    Subject save(Subject subject);

    List<Subject> findAll();
}
