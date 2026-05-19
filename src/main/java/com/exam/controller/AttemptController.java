package com.exam.controller;

import com.exam.model.Attempt;
import com.exam.model.Result;
import com.exam.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "*")
public class AttemptController {

    @Autowired
    private AttemptService attemptService;

    @PostMapping("/start")
    public Attempt startExam(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long examId = request.get("examId");
        return attemptService.startExam(studentId, examId);
    }

    @PutMapping("/{attemptId}/answer")
    public String saveAnswer(@PathVariable Long attemptId, @RequestBody Map<String, Object> request) {
        Long questionId = Long.valueOf(request.get("questionId").toString());
        String answer = request.get("answer").toString();
        attemptService.saveAnswer(attemptId, questionId, answer);
        return "Answer saved";
    }

    @PostMapping("/{attemptId}/submit")
    public Result submitExam(@PathVariable Long attemptId) {
        return attemptService.submitExam(attemptId);
    }

    @GetMapping("/student/{studentId}")
    public List<Attempt> getAttemptsByStudent(@PathVariable Long studentId) {
        return attemptService.getAttemptsByStudent(studentId);
    }
}
