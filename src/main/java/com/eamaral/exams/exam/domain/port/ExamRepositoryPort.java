package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

import java.util.List;

public interface ExamRepositoryPort {

    Exam save(Exam exam);

    List<Exam> findByUser(String currentUser);

    List<Exam> findAvailable();
}
