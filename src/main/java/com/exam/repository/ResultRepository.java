package com.exam.repository;

import com.exam.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudent_StudentId(Long studentId);
    List<Result> findByExam_ExamId(Long examId);
}
