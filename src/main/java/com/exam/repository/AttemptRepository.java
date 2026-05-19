package com.exam.repository;

import com.exam.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    boolean existsByStudent_StudentIdAndExam_ExamId(Long studentId, Long examId);
    Optional<Attempt> findByStudent_StudentIdAndExam_ExamId(Long studentId, Long examId);
}
