package com.exam.service;

import com.exam.model.Exam;
import com.exam.model.Course;
import com.exam.repository.ExamRepository;
import com.exam.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Exam createExam(Exam exam, Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        exam.setCourse(course);
        return examRepository.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExam(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    public List<Exam> getExamsByCourse(Long courseId) {
        return examRepository.findByCourse_CourseId(courseId);
    }
}
