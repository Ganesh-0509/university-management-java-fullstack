package com.exam.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.exam.model.Attempt;
import com.exam.model.Course;
import com.exam.model.Exam;
import com.exam.model.Question;
import com.exam.model.Student;
import com.exam.service.AttemptService;
import com.exam.service.CourseService;
import com.exam.service.ExamService;
import com.exam.service.QuestionService;
import com.exam.service.StudentService;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AttemptService attemptService;

    @Override
    public void run(String... args) {
        if (!studentService.getAllStudents().isEmpty()) {
            return;
        }

        Course course1 = new Course();
        course1.setCourseName("Intro to Programming");
        course1.setSemester(1);
        course1.setCredits(4);
        course1 = courseService.createCourse(course1);

        Course course2 = new Course();
        course2.setCourseName("Database Systems");
        course2.setSemester(2);
        course2.setCredits(3);
        course2 = courseService.createCourse(course2);

        Student student1 = new Student();
        student1.setName("Alice Johnson");
        student1.setEmail("alice.johnson@example.com");
        student1.setDepartment("Computer Science");
        student1.setSemester(1);
        student1 = studentService.registerStudent(student1);
        studentService.enrollStudent(student1.getStudentId(), course1.getCourseId());

        Student student2 = new Student();
        student2.setName("Bob Patel");
        student2.setEmail("bob.patel@example.com");
        student2.setDepartment("Computer Science");
        student2.setSemester(1);
        student2 = studentService.registerStudent(student2);
        studentService.enrollStudent(student2.getStudentId(), course1.getCourseId());

        Student student3 = new Student();
        student3.setName("Carla Singh");
        student3.setEmail("carla.singh@example.com");
        student3.setDepartment("Information Systems");
        student3.setSemester(2);
        student3 = studentService.registerStudent(student3);
        studentService.enrollStudent(student3.getStudentId(), course2.getCourseId());

        Exam exam1 = new Exam();
        exam1.setExamName("Programming Basics");
        exam1.setDuration(30);
        exam1.setTotalMarks(100);
        exam1.setExamDate(LocalDate.now().plusDays(7));
        exam1 = examService.createExam(exam1, course1.getCourseId());

        Exam exam2 = new Exam();
        exam2.setExamName("Database Fundamentals");
        exam2.setDuration(45);
        exam2.setTotalMarks(100);
        exam2.setExamDate(LocalDate.now().plusDays(10));
        exam2 = examService.createExam(exam2, course2.getCourseId());

        Question q1 = new Question();
        q1.setQuestionText("What is the keyword used to define a class in Java?");
        q1.setOptionA("class");
        q1.setOptionB("object");
        q1.setOptionC("define");
        q1.setOptionD("new");
        q1.setCorrectAnswer("A");
        q1 = questionService.addQuestion(q1, exam1.getExamId());

        Question q2 = new Question();
        q2.setQuestionText("Which command is used to create a new table in SQL?");
        q2.setOptionA("SELECT");
        q2.setOptionB("CREATE TABLE");
        q2.setOptionC("INSERT INTO");
        q2.setOptionD("UPDATE");
        q2.setCorrectAnswer("B");
        q2 = questionService.addQuestion(q2, exam2.getExamId());

        Question q3 = new Question();
        q3.setQuestionText("Which loop is best when you know the number of iterations?");
        q3.setOptionA("while");
        q3.setOptionB("do-while");
        q3.setOptionC("for");
        q3.setOptionD("until");
        q3.setCorrectAnswer("C");
        questionService.addQuestion(q3, exam1.getExamId());

        Question q4 = new Question();
        q4.setQuestionText("What does SQL stand for?");
        q4.setOptionA("Structured Query Language");
        q4.setOptionB("Simple Query Language");
        q4.setOptionC("Standard Query List");
        q4.setOptionD("Structured Question Language");
        q4.setCorrectAnswer("A");
        questionService.addQuestion(q4, exam2.getExamId());

        Question q5 = new Question();
        q5.setQuestionText("Which operator is used for equality in Java?");
        q5.setOptionA("=");
        q5.setOptionB("==");
        q5.setOptionC("===");
        q5.setOptionD("!=");
        q5.setCorrectAnswer("B");
        questionService.addQuestion(q5, exam1.getExamId());

        Question q6 = new Question();
        q6.setQuestionText("Which SQL clause is used to filter rows?");
        q6.setOptionA("GROUP BY");
        q6.setOptionB("ORDER BY");
        q6.setOptionC("WHERE");
        q6.setOptionD("HAVING");
        q6.setCorrectAnswer("C");
        questionService.addQuestion(q6, exam2.getExamId());

        Attempt attempt1 = attemptService.startExam(student1.getStudentId(), exam1.getExamId());
        if (attempt1 != null) {
            attemptService.saveAnswer(attempt1.getAttemptId(), q1.getQuestionId(), "A");
            attemptService.saveAnswer(attempt1.getAttemptId(), q3.getQuestionId(), "C");
            attemptService.saveAnswer(attempt1.getAttemptId(), q5.getQuestionId(), "B");
            attemptService.submitExam(attempt1.getAttemptId());
        }

        Attempt attempt2 = attemptService.startExam(student2.getStudentId(), exam1.getExamId());
        if (attempt2 != null) {
            attemptService.saveAnswer(attempt2.getAttemptId(), q1.getQuestionId(), "B");
            attemptService.saveAnswer(attempt2.getAttemptId(), q3.getQuestionId(), "C");
            attemptService.saveAnswer(attempt2.getAttemptId(), q5.getQuestionId(), "A");
            attemptService.submitExam(attempt2.getAttemptId());
        }

        Attempt attempt3 = attemptService.startExam(student3.getStudentId(), exam2.getExamId());
        if (attempt3 != null) {
            attemptService.saveAnswer(attempt3.getAttemptId(), q2.getQuestionId(), "B");
            attemptService.saveAnswer(attempt3.getAttemptId(), q4.getQuestionId(), "A");
            attemptService.saveAnswer(attempt3.getAttemptId(), q6.getQuestionId(), "C");
            attemptService.submitExam(attempt3.getAttemptId());
        }

        System.out.println("Loaded mock sample data for CLI usage.");
    }
}
