package com.exam.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.exam.model.Attempt;
import com.exam.model.Course;
import com.exam.model.Exam;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.model.Student;
import com.exam.service.AttemptService;
import com.exam.service.CourseService;
import com.exam.service.ExamService;
import com.exam.service.QuestionService;
import com.exam.service.ResultService;
import com.exam.service.SqlService;
import com.exam.service.StudentService;

@Component
@Order(2)
public class AppCli implements CommandLineRunner {

    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private ExamService examService;
    @Autowired private QuestionService questionService;
    @Autowired private AttemptService attemptService;
    @Autowired private ResultService resultService;
    @Autowired private SqlService sqlService;

    @Override
    public void run(String... args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("\n===== University Exam System CLI =====");
            System.out.println("Enter a menu number, or type 'help' for text commands, 'menu' to show the menu again, 'exit' to quit.\n");
            printMainMenu();

            while (true) {
                System.out.print("> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("exit")) break;
                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("menu")) {
                    printMainMenu();
                    continue;
                }
                if (line.matches("\\d+")) {
                    showMenuChoice(Integer.parseInt(line), sc);
                    continue;
                }
                try {
                    handle(parse(line));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("Bye!");
        }
    }

    private void showMenuChoice(int choice, Scanner sc) {
        switch (choice) {
            case 1 -> showStudentMenu(sc);
            case 2 -> showCourseMenu(sc);
            case 3 -> showExamMenu(sc);
            case 4 -> showQuestionMenu(sc);
            case 5 -> showAttemptMenu(sc);
            case 6 -> showResultMenu(sc);
            case 7 -> showSqlMenu(sc);
            default -> System.out.println("Invalid menu selection. Type 'menu' to show the options.");
        }
    }

    private void printMainMenu() {
        System.out.println("MAIN MENU:");
        System.out.println(" 1) Student operations");
        System.out.println(" 2) Course operations");
        System.out.println(" 3) Exam operations");
        System.out.println(" 4) Question operations");
        System.out.println(" 5) Attempt operations");
        System.out.println(" 6) Result operations");
        System.out.println(" 7) Run SQL query");
        System.out.println("Type the number to choose a section, or 'help' for full commands.\n");
    }

    private void showStudentMenu(Scanner sc) {
        System.out.println("STUDENT MENU:");
        System.out.println(" 1) List students");
        System.out.println(" 2) Get student by ID");
        System.out.println(" 3) Add student");
        System.out.println(" 4) Enroll student in course");
        System.out.println(" 0) Back to main menu");
        System.out.print("student> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> handle(List.of("student-list"));
            case "2" -> {
                long id = readLong(sc, "Student ID: ");
                handle(List.of("student-get", Long.toString(id)));
            }
            case "3" -> {
                String name = readText(sc, "Name: ");
                String email = readText(sc, "Email: ");
                String dept = readText(sc, "Department: ");
                int sem = readInt(sc, "Semester: ");
                handle(List.of("student-add", name, email, dept, Integer.toString(sem)));
            }
            case "4" -> {
                long sid = readLong(sc, "Student ID: ");
                long cid = readLong(sc, "Course ID: ");
                handle(List.of("student-enroll", Long.toString(sid), Long.toString(cid)));
            }
            default -> System.out.println("Invalid student menu choice.");
        }
        printMainMenu();
    }

    private void showCourseMenu(Scanner sc) {
        System.out.println("COURSE MENU:");
        System.out.println(" 1) List courses");
        System.out.println(" 2) Get course by ID");
        System.out.println(" 3) Add course");
        System.out.println(" 0) Back to main menu");
        System.out.print("course> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> handle(List.of("course-list"));
            case "2" -> {
                long id = readLong(sc, "Course ID: ");
                handle(List.of("course-get", Long.toString(id)));
            }
            case "3" -> {
                String name = readText(sc, "Course name: ");
                int sem = readInt(sc, "Semester: ");
                int credits = readInt(sc, "Credits: ");
                handle(List.of("course-add", name, Integer.toString(sem), Integer.toString(credits)));
            }
            default -> System.out.println("Invalid course menu choice.");
        }
        printMainMenu();
    }

    private void showExamMenu(Scanner sc) {
        System.out.println("EXAM MENU:");
        System.out.println(" 1) List exams");
        System.out.println(" 2) Get exam by ID");
        System.out.println(" 3) Add exam");
        System.out.println(" 4) List exams by course");
        System.out.println(" 0) Back to main menu");
        System.out.print("exam> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> handle(List.of("exam-list"));
            case "2" -> {
                long id = readLong(sc, "Exam ID: ");
                handle(List.of("exam-get", Long.toString(id)));
            }
            case "3" -> {
                String name = readText(sc, "Exam name: ");
                long courseId = readLong(sc, "Course ID: ");
                int duration = readInt(sc, "Duration (minutes): ");
                int marks = readInt(sc, "Total marks: ");
                String date = readText(sc, "Date (YYYY-MM-DD): ");
                handle(List.of("exam-add", name, Long.toString(courseId), Integer.toString(duration), Integer.toString(marks), date));
            }
            case "4" -> {
                long cid = readLong(sc, "Course ID: ");
                handle(List.of("exam-by-course", Long.toString(cid)));
            }
            default -> System.out.println("Invalid exam menu choice.");
        }
        printMainMenu();
    }

    private void showQuestionMenu(Scanner sc) {
        System.out.println("QUESTION MENU:");
        System.out.println(" 1) List questions by exam");
        System.out.println(" 2) Add question");
        System.out.println(" 0) Back to main menu");
        System.out.print("question> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> {
                long examId = readLong(sc, "Exam ID: ");
                handle(List.of("question-list", Long.toString(examId)));
            }
            case "2" -> {
                long examId = readLong(sc, "Exam ID: ");
                String text = readText(sc, "Question text: ");
                String a = readText(sc, "Option A: ");
                String b = readText(sc, "Option B: ");
                String c = readText(sc, "Option C: ");
                String d = readText(sc, "Option D: ");
                String answer = readText(sc, "Correct answer: ");
                handle(List.of("question-add", Long.toString(examId), text, a, b, c, d, answer));
            }
            default -> System.out.println("Invalid question menu choice.");
        }
        printMainMenu();
    }

    private void showAttemptMenu(Scanner sc) {
        System.out.println("ATTEMPT MENU:");
        System.out.println(" 1) Start exam");
        System.out.println(" 2) Answer question");
        System.out.println(" 3) Submit exam");
        System.out.println(" 4) List attempts by student");
        System.out.println(" 0) Back to main menu");
        System.out.print("attempt> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> {
                long sid = readLong(sc, "Student ID: ");
                long eid = readLong(sc, "Exam ID: ");
                handle(List.of("exam-start", Long.toString(sid), Long.toString(eid)));
            }
            case "2" -> {
                long aid = readLong(sc, "Attempt ID: ");
                long qid = readLong(sc, "Question ID: ");
                String ans = readText(sc, "Answer: ");
                handle(List.of("answer", Long.toString(aid), Long.toString(qid), ans));
            }
            case "3" -> {
                long aid = readLong(sc, "Attempt ID: ");
                handle(List.of("exam-submit", Long.toString(aid)));
            }
            case "4" -> {
                long sid = readLong(sc, "Student ID: ");
                handle(List.of("attempt-list", Long.toString(sid)));
            }
            default -> System.out.println("Invalid attempt menu choice.");
        }
        printMainMenu();
    }

    private void showResultMenu(Scanner sc) {
        System.out.println("RESULT MENU:");
        System.out.println(" 1) Get result by ID");
        System.out.println(" 2) Get results by student");
        System.out.println(" 3) Get results by exam");
        System.out.println(" 0) Back to main menu");
        System.out.print("result> ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;
        if (input.equals("0")) { printMainMenu(); return; }
        switch (input) {
            case "1" -> {
                long id = readLong(sc, "Result ID: ");
                handle(List.of("result-get", Long.toString(id)));
            }
            case "2" -> {
                long sid = readLong(sc, "Student ID: ");
                handle(List.of("result-student", Long.toString(sid)));
            }
            case "3" -> {
                long eid = readLong(sc, "Exam ID: ");
                handle(List.of("result-exam", Long.toString(eid)));
            }
            default -> System.out.println("Invalid result menu choice.");
        }
        printMainMenu();
    }

    private void showSqlMenu(Scanner sc) {
        System.out.println("SQL MENU:");
        System.out.println("Enter SQL statement and finish with a semicolon ';' on its own line.");
        System.out.println("Example: SELECT * FROM students;");
        System.out.println("Type 'back' to return to the main menu.");
        System.out.println();
        StringBuilder sql = new StringBuilder();
        while (true) {
            System.out.print("sql> ");
            String line = sc.nextLine();
            if (line == null) break;
            if (line.trim().equalsIgnoreCase("back")) {
                printMainMenu();
                return;
            }
            sql.append(line).append(" ");
            if (line.trim().endsWith(";")) {
                break;
            }
        }
        String query = sql.toString().trim();
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1).trim();
        }
        if (query.isEmpty()) {
            System.out.println("No SQL entered.");
            printMainMenu();
            return;
        }
        executeSql(query);
        printMainMenu();
    }

    private void executeSql(String query) {
        try {
            String normalized = query.strip().toLowerCase();
            if (normalized.startsWith("select") || normalized.startsWith("with")) {
                List<Map<String, Object>> rows = sqlService.executeQuery(query);
                if (rows.isEmpty()) {
                    System.out.println("Query returned no rows.");
                    return;
                }
                List<String> columns = new ArrayList<>(rows.get(0).keySet());
                String header = String.join(" | ", columns);
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                for (Map<String, Object> row : rows) {
                    StringBuilder line = new StringBuilder();
                    for (String col : columns) {
                        line.append(row.get(col)).append(" | ");
                    }
                    System.out.println(line.substring(0, line.length() - 3));
                }
            } else {
                int count = sqlService.executeUpdate(query);
                System.out.println("Statement executed, affected rows: " + count);
            }
        } catch (Exception e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    private long readLong(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readText(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
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
