package com.exam.controller;

import com.exam.model.Result;
import com.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/{id}")
    public Result getResult(@PathVariable Long id) {
        return resultService.getResult(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Result> getResultsByStudent(@PathVariable Long studentId) {
        return resultService.getResultsByStudent(studentId);
    }

    @GetMapping("/exam/{examId}")
    public List<Result> getResultsByExam(@PathVariable Long examId) {
        return resultService.getResultsByExam(examId);
    }
}
