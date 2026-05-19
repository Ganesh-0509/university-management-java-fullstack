package com.exam.controller;

import com.exam.model.Exam;
import com.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/{courseId}")
    public Exam createExam(@RequestBody Exam exam, @PathVariable Long courseId) {
        return examService.createExam(exam, courseId);
    }

    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/{id}")
    public Exam getExam(@PathVariable Long id) {
        return examService.getExam(id);
    }

    @GetMapping("/course/{courseId}")
    public List<Exam> getExamsByCourse(@PathVariable Long courseId) {
        return examService.getExamsByCourse(courseId);
    }
}
