package com.exam.service;

import com.exam.model.Result;
import com.exam.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public List<Result> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudent_StudentId(studentId);
    }

    public List<Result> getResultsByExam(Long examId) {
        return resultRepository.findByExam_ExamId(examId);
    }

    public Result getResult(Long id) {
        return resultRepository.findById(id).orElse(null);
    }
}
