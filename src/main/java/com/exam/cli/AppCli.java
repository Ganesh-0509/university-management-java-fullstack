package com.exam.cli;

import com.exam.model.*;
import com.exam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class AppCli implements CommandLineRunner {

    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private ExamService examService;
    @Autowired private QuestionService questionService;
    @Autowired private AttemptService attemptService;
    @Autowired private ResultService resultService;

    @Override
    public void run(String... args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("\n===== University Exam System CLI =====");
            System.out.println("Type 'help' for commands, 'exit' to quit.\n");

            while (true) {
                System.out.print("> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.equals("exit")) break;

                try {
                    handle(parse(line));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("Bye!");
        }
    }

    private List<String> parse(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (sb.length() > 0) { tokens.add(sb.toString()); sb.setLength(0); }
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) tokens.add(sb.toString());
        return tokens;
    }

    private void handle(List<String> cmd) {
        String action = cmd.get(0);

        switch (action) {
            case "help" -> printHelp();

            // ==================== STUDENT ====================
            case "student-list" -> {
                List<Student> list = studentService.getAllStudents();
                if (list.isEmpty()) { System.out.println("No students."); return; }
                System.out.printf("%-5s %-20s %-25s %-15s %-6s%n", "ID", "Name", "Email", "Dept", "Sem");
                System.out.println("-".repeat(71));
                for (Student s : list)
                    System.out.printf("%-5d %-20s %-25s %-15s %-6d%n",
                            s.getStudentId(), s.getName(), s.getEmail(), s.getDepartment(), s.getSemester());
            }
            case "student-get" -> {
                var s = studentService.getStudent(Long.parseLong(cmd.get(1)));
                if (s == null) { System.out.println("Not found."); return; }
                System.out.printf("ID: %d | Name: %s | Email: %s | Dept: %s | Sem: %d%n",
                        s.getStudentId(), s.getName(), s.getEmail(), s.getDepartment(), s.getSemester());
            }
            case "student-add" -> {
                Student s = new Student();
                s.setName(cmd.get(1)); s.setEmail(cmd.get(2));
                s.setDepartment(cmd.get(3)); s.setSemester(Integer.parseInt(cmd.get(4)));
                System.out.println("Created student ID: " + studentService.registerStudent(s).getStudentId());
            }
            case "student-enroll" -> {
                studentService.enrollStudent(Long.parseLong(cmd.get(1)), Long.parseLong(cmd.get(2)));
                System.out.println("Enrolled.");
            }

            // ==================== COURSE ====================
            case "course-list" -> {
                List<Course> list = courseService.getAllCourses();
                if (list.isEmpty()) { System.out.println("No courses."); return; }
                System.out.printf("%-5s %-30s %-6s %-8s%n", "ID", "Name", "Sem", "Credits");
                System.out.println("-".repeat(50));
                for (Course c : list)
                    System.out.printf("%-5d %-30s %-6d %-8d%n",
                            c.getCourseId(), c.getCourseName(), c.getSemester(), c.getCredits());
            }
            case "course-get" -> {
                Course c = courseService.getCourse(Long.parseLong(cmd.get(1)));
                if (c == null) { System.out.println("Not found."); return; }
                System.out.printf("ID: %d | Name: %s | Sem: %d | Credits: %d%n",
                        c.getCourseId(), c.getCourseName(), c.getSemester(), c.getCredits());
            }
            case "course-add" -> {
                Course c = new Course();
                c.setCourseName(cmd.get(1)); c.setSemester(Integer.parseInt(cmd.get(2)));
                c.setCredits(Integer.parseInt(cmd.get(3)));
                System.out.println("Created course ID: " + courseService.createCourse(c).getCourseId());
            }

            // ==================== EXAM ====================
            case "exam-list" -> {
                List<Exam> list = examService.getAllExams();
                if (list.isEmpty()) { System.out.println("No exams."); return; }
                System.out.printf("%-5s %-25s %-12s %-8s %-8s %-12s%n", "ID", "Name", "Course", "Dur", "Marks", "Date");
                System.out.println("-".repeat(75));
                for (Exam e : list) {
                    String cn = e.getCourse() != null ? e.getCourse().getCourseName() : "N/A";
                    String dt = e.getExamDate() != null ? e.getExamDate().toString() : "N/A";
                    System.out.printf("%-5d %-25s %-12s %-8d %-8d %-12s%n",
                            e.getExamId(), e.getExamName(), cn, e.getDuration(), e.getTotalMarks(), dt);
                }
            }
            case "exam-get" -> {
                Exam e = examService.getExam(Long.parseLong(cmd.get(1)));
                if (e == null) { System.out.println("Not found."); return; }
                String cn = e.getCourse() != null ? e.getCourse().getCourseName() : "N/A";
                System.out.printf("ID: %d | Name: %s | Course: %s | Duration: %d min | Marks: %d | Date: %s%n",
                        e.getExamId(), e.getExamName(), cn, e.getDuration(), e.getTotalMarks(),
                        e.getExamDate() != null ? e.getExamDate() : "N/A");
            }
            case "exam-add" -> {
                Exam e = new Exam();
                e.setExamName(cmd.get(1)); e.setDuration(Integer.parseInt(cmd.get(3)));
                e.setTotalMarks(Integer.parseInt(cmd.get(4)));
                e.setExamDate(java.time.LocalDate.parse(cmd.get(5)));
                System.out.println("Created exam ID: " + examService.createExam(e, Long.parseLong(cmd.get(2))).getExamId());
            }
            case "exam-by-course" -> {
                List<Exam> list = examService.getExamsByCourse(Long.parseLong(cmd.get(1)));
                if (list.isEmpty()) { System.out.println("No exams for that course."); return; }
                for (Exam e : list)
                    System.out.printf("  [%d] %s - %d marks, %d min%n", e.getExamId(), e.getExamName(), e.getTotalMarks(), e.getDuration());
            }

            // ==================== QUESTION ====================
            case "question-list" -> {
                List<Question> list = questionService.getQuestionsByExam(Long.parseLong(cmd.get(1)));
                if (list.isEmpty()) { System.out.println("No questions."); return; }
                for (Question q : list) {
                    System.out.printf("[%d] %s%n", q.getQuestionId(), q.getQuestionText());
                    System.out.printf("   A) %s  B) %s  C) %s  D) %s%n", q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD());
                    System.out.printf("   Answer: %s%n%n", q.getCorrectAnswer());
                }
            }
            case "question-add" -> {
                Question q = new Question();
                q.setQuestionText(cmd.get(2)); q.setOptionA(cmd.get(3)); q.setOptionB(cmd.get(4));
                q.setOptionC(cmd.get(5)); q.setOptionD(cmd.get(6)); q.setCorrectAnswer(cmd.get(7));
                System.out.println("Added question ID: " + questionService.addQuestion(q, Long.parseLong(cmd.get(1))).getQuestionId());
            }

            // ==================== ATTEMPT ====================
            case "exam-start" -> {
                Attempt a = attemptService.startExam(Long.parseLong(cmd.get(1)), Long.parseLong(cmd.get(2)));
                if (a == null) { System.out.println("Cannot start. Check IDs or duplicate."); return; }
                System.out.println("Started! Attempt ID: " + a.getAttemptId());
                System.out.println("Use: answer <attemptId> <questionId> <answer>");
            }
            case "answer" -> {
                attemptService.saveAnswer(Long.parseLong(cmd.get(1)), Long.parseLong(cmd.get(2)), cmd.get(3));
                System.out.println("Saved.");
            }
            case "exam-submit" -> {
                Result r = attemptService.submitExam(Long.parseLong(cmd.get(1)));
                if (r == null) { System.out.println("Failed (timed out?)."); return; }
                System.out.printf("Score: %d | %.2f%% | %s%n", r.getScore(), r.getPercentage(), r.getResultStatus());
            }
            case "attempt-list" -> {
                List<Attempt> list = attemptService.getAttemptsByStudent(Long.parseLong(cmd.get(1)));
                if (list.isEmpty()) { System.out.println("No attempts."); return; }
                System.out.printf("%-8s %-25s %-8s %-10s%n", "AttID", "Exam", "Score", "Status");
                System.out.println("-".repeat(55));
                for (Attempt a : list) {
                    String en = a.getExam() != null ? a.getExam().getExamName() : "N/A";
                    System.out.printf("%-8d %-25s %-8d %-10s%n", a.getAttemptId(), en, a.getScore(), a.getStatus());
                }
            }

            // ==================== RESULT ====================
            case "result-get" -> {
                Result r = resultService.getResult(Long.parseLong(cmd.get(1)));
                if (r == null) { System.out.println("Not found."); return; }
                String en = r.getExam() != null ? r.getExam().getExamName() : "N/A";
                String sn = r.getStudent() != null ? r.getStudent().getName() : "N/A";
                System.out.printf("ID: %d | Student: %s | Exam: %s | Score: %d | %.2f%% | %s%n",
                        r.getResultId(), sn, en, r.getScore(), r.getPercentage(), r.getResultStatus());
            }
            case "result-student" -> {
                List<Result> list = resultService.getResultsByStudent(Long.parseLong(cmd.get(1)));
                if (list.isEmpty()) { System.out.println("No results."); return; }
                System.out.printf("%-8s %-25s %-8s %-12s %-8s%n", "ID", "Exam", "Score", "Percent", "Status");
                System.out.println("-".repeat(65));
                for (Result r : list) {
                    String en = r.getExam() != null ? r.getExam().getExamName() : "N/A";
                    System.out.printf("%-8d %-25s %-8d %-12.2f %-8s%n",
                            r.getResultId(), en, r.getScore(), r.getPercentage(), r.getResultStatus());
                }
            }
            case "result-exam" -> {
                List<Result> list = resultService.getResultsByExam(Long.parseLong(cmd.get(1)));
                if (list.isEmpty()) { System.out.println("No results."); return; }
                System.out.printf("%-8s %-20s %-8s %-12s %-8s%n", "ID", "Student", "Score", "Percent", "Status");
                System.out.println("-".repeat(60));
                for (Result r : list) {
                    String sn = r.getStudent() != null ? r.getStudent().getName() : "N/A";
                    System.out.printf("%-8d %-20s %-8d %-12.2f %-8s%n",
                            r.getResultId(), sn, r.getScore(), r.getPercentage(), r.getResultStatus());
                }
            }

            default -> System.out.println("Unknown command. Type 'help'.");
        }
    }

    private void printHelp() {
        System.out.println("STUDENT                          COURSE");
        System.out.println("-------                          ------");
        System.out.println("student-list                     course-list");
        System.out.println("student-get <id>                 course-get <id>");
        System.out.println("student-add <name> <email>       course-add <name> <sem> <credits>");
        System.out.println("  <dept> <sem>");
        System.out.println("student-enroll <sid> <cid>");
        System.out.println();
        System.out.println("EXAM                             QUESTION");
        System.out.println("----                             --------");
        System.out.println("exam-list                        question-list <examId>");
        System.out.println("exam-get <id>                    question-add <examId> <text>");
        System.out.println("exam-add <name> <cid>              <A> <B> <C> <D> <correct>");
        System.out.println("  <duration> <marks> <date>");
        System.out.println("exam-by-course <cid>");
        System.out.println();
        System.out.println("ATTEMPT                          RESULT");
        System.out.println("-------                          ------");
        System.out.println("exam-start <sid> <eid>           result-get <id>");
        System.out.println("answer <aid> <qid> <ans>         result-student <sid>");
        System.out.println("exam-submit <aid>                result-exam <eid>");
        System.out.println("attempt-list <sid>");
        System.out.println();
        System.out.println("exit  - quit");
    }
}
