package com.exam.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    private String examName;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int duration;
    private int totalMarks;
    private LocalDate examDate;

    public Exam() {}

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
}
