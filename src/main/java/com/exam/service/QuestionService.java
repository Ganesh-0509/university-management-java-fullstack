package com.exam.service;

import com.exam.model.Question;
import com.exam.model.Exam;
import com.exam.repository.QuestionRepository;
import com.exam.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    public Question addQuestion(Question question, Long examId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        question.setExam(exam);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByExam(Long examId) {
        return questionRepository.findByExam_ExamId(examId);
    }
}
