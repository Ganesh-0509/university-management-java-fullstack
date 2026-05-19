package com.exam.repository;

import com.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse_CourseId(Long courseId);
}
