package com.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.model.Course;
import com.exam.model.Student;
import com.exam.repository.CourseRepository;
import com.exam.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        if (student != null && course != null) {
            student.getCourses().add(course);
            studentRepository.save(student);
        }
    }
}
