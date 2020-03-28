package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamRepositoryPort {

    Exam save(Exam exam);

    Optional<Exam> findById(String id, String currentUser);

    List<Exam> findByUser(String currentUser);

    void delete(Exam exam);
}
