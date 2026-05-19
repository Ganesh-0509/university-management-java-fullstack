package com.exam.controller;

import com.exam.model.Student;
import com.exam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student registerStudent(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public String enrollStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.enrollStudent(studentId, courseId);
        return "Student enrolled successfully";
    }
}
