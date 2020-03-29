package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

public interface ExamRepositoryPort {

    Exam save(Exam exam);
}
