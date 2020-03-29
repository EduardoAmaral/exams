package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

public interface ExamPort {

    void create(Exam exam, String currentUserId);
}
