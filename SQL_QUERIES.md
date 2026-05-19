## 1) Top scorers per course

Returns the highest score earned in each course and the student(s) who achieved it.

```sql
WITH course_top AS (
  SELECT c.course_id,
         c.course_name,
         MAX(r.score) AS max_score
  FROM results r
  JOIN exams e ON r.exam_id = e.exam_id
  JOIN courses c ON e.course_id = c.course_id
  GROUP BY c.course_id, c.course_name
)
SELECT ct.course_id,
       ct.course_name,
       r.student_id,
       s.name AS student_name,
       r.score
FROM course_top ct
JOIN results r ON r.score = ct.max_score
JOIN exams e ON r.exam_id = e.exam_id AND e.course_id = ct.course_id
JOIN students s ON r.student_id = s.student_id
ORDER BY ct.course_id, s.name;
```

## 2) Average pass percentage per course

Returns the average percentage score across all passing results for each course.

```sql
SELECT c.course_id,
       c.course_name,
       AVG(r.percentage) FILTER (WHERE r.result_status = 'PASS') AS avg_pass_percentage
FROM results r
JOIN exams e ON r.exam_id = e.exam_id
JOIN courses c ON e.course_id = c.course_id
GROUP BY c.course_id, c.course_name
ORDER BY c.course_id;
```
