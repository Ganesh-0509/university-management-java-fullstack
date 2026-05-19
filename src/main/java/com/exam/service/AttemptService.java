package com.exam.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.model.Attempt;
import com.exam.model.Exam;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.model.Student;
import com.exam.repository.AttemptRepository;
import com.exam.repository.ExamRepository;
import com.exam.repository.QuestionRepository;
import com.exam.repository.ResultRepository;
import com.exam.repository.StudentRepository;

@Service
public class AttemptService {

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    public Attempt startExam(Long studentId, Long examId) {
        // Check duplicate attempt
        if (attemptRepository.existsByStudent_StudentIdAndExam_ExamId(studentId, examId)) {
            return null; // Already attempted
        }

        Student student = studentRepository.findById(studentId).orElse(null);
        Exam exam = examRepository.findById(examId).orElse(null);

        if (student == null || exam == null) return null;

        Attempt attempt = new Attempt();
        attempt.setStudent(student);
        attempt.setExam(exam);
        attempt.setSubmittedAnswers(new HashMap<>());
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus("IN_PROGRESS");

        return attemptRepository.save(attempt);
    }

    @Transactional
    public void saveAnswer(Long attemptId, Long questionId, String answer) {
        Attempt attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt != null && "IN_PROGRESS".equals(attempt.getStatus())) {
            if (attempt.getSubmittedAnswers() == null) {
                attempt.setSubmittedAnswers(new HashMap<>());
            }
            attempt.getSubmittedAnswers().put(questionId, answer);
            attemptRepository.save(attempt);
        }
    }

    @Transactional
    public Result submitExam(Long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt == null) return null;

        // Check timer
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = attempt.getStartTime().plusMinutes(attempt.getExam().getDuration());
        if (now.isAfter(deadline)) {
            attempt.setStatus("TIMED_OUT");
            attemptRepository.save(attempt);
            return null;
        }

        // Evaluate answers
        List<Question> questions = questionRepository.findByExam_ExamId(attempt.getExam().getExamId());
        int correct = 0;
        for (Question q : questions) {
            String submitted = attempt.getSubmittedAnswers().get(q.getQuestionId());
            if (submitted != null && submitted.equalsIgnoreCase(q.getCorrectAnswer())) {
                correct++;
            }
        }

        int totalQuestions = questions.size();
        int totalMarks = attempt.getExam().getTotalMarks();
        int score = totalQuestions > 0 ? (correct * totalMarks) / totalQuestions : 0;
        double percentage = totalMarks > 0 ? (double) score / totalMarks * 100 : 0;

        // Update attempt
        attempt.setScore(score);
        attempt.setEndTime(now);
        attempt.setStatus("SUBMITTED");
        attemptRepository.save(attempt);

        // Create result
        Result result = new Result();
        result.setStudent(attempt.getStudent());
        result.setExam(attempt.getExam());
        result.setScore(score);
        result.setPercentage(Math.round(percentage * 100.0) / 100.0);
        result.setResultStatus(percentage >= 40 ? "PASS" : "FAIL");

        return resultRepository.save(result);
    }

    public List<Attempt> getAttemptsByStudent(Long studentId) {
        return attemptRepository.findAll().stream()
                .filter(a -> a.getStudent().getStudentId().equals(studentId))
                .toList();
    }
}
