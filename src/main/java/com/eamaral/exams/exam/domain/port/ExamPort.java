package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

import java.util.List;

public interface ExamPort {

    void create(Exam exam, String currentUser);

    List<Exam> findByUser(String currentUser);

    List<Exam> findAvailable();
}
