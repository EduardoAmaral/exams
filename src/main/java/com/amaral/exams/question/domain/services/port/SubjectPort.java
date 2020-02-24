package com.amaral.exams.question.domain.services.port;

import com.amaral.exams.question.domain.Subject;

import java.util.List;

public interface SubjectPort {

    Subject save(Subject subject);

    List<Subject> findAll();
}
