package com.exam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "attempts",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "exam_id"}))
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ElementCollection
    @CollectionTable(name = "attempt_answers", joinColumns = @JoinColumn(name = "attempt_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer")
    private Map<Long, String> submittedAnswers;

    private int score;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Attempt() {}

    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Map<Long, String> getSubmittedAnswers() { return submittedAnswers; }
    public void setSubmittedAnswers(Map<Long, String> submittedAnswers) { this.submittedAnswers = submittedAnswers; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
