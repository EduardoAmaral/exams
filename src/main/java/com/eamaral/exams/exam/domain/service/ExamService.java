package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamPort;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class ExamService implements ExamPort {

    @Override
    public void save(Exam exam) {

    }

    @Override
    public List<Exam> findByUser(String currentUserId) {
        return emptyList();
    }

}
