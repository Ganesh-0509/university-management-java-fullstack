package com.exam.controller;

import com.exam.model.Question;
import com.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/exams/{examId}/questions")
    public Question addQuestion(@RequestBody Question question, @PathVariable Long examId) {
        return questionService.addQuestion(question, examId);
    }

    @GetMapping("/exams/{examId}/questions")
    public List<Question> getQuestionsByExam(@PathVariable Long examId) {
        return questionService.getQuestionsByExam(examId);
    }
}
