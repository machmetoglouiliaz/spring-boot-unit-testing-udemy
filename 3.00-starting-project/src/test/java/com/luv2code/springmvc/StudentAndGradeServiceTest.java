package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @BeforeEach
    public void beforeEach(){

        jdbc.execute("INSERT INTO student(firstname, lastname, email_address) " +
                "VALUES ('Mourat', 'Achmet', 'm@g.com')");
        jdbc.execute("insert into math_grade(student_id, grade) values (1, 100.00)");
        jdbc.execute("insert into science_grade(student_id, grade) values (1, 100.00)");
        jdbc.execute("insert into history_grade(student_id, grade) values (1, 100.00)");
    }

    @AfterEach
    public void afterEach(){

        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");

        jdbc.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
        jdbc.execute("ALTER TABLE math_grade ALTER COLUMN ID RESTART WITH 1");
        jdbc.execute("ALTER TABLE science_grade ALTER COLUMN ID RESTART WITH 1");
        jdbc.execute("ALTER TABLE history_grade ALTER COLUMN ID RESTART WITH 1");

    }

    @Test
    public void createStudentService(){

        //studentService.createStudent("Mourat", "Achmet", "m@g.com");

        CollegeStudent student = studentDao.findByEmailAddress("m@g.com");

        assertEquals("m@g.com", student.getEmailAddress(), "find by email not responded as expected");
    }

    @Test
    public void isStudentNull(){

        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService(){
        Optional<CollegeStudent> student = studentDao.findById(1);

        assertTrue(student.isPresent(), "Didnt find student with id 1");

        studentService.deleteStudent(1);
        student = studentDao.findById(1);

        assertFalse(student.isPresent(), "Shouldnt find a student with id 1");
    }

    @Sql("/insertData.sql")
    @Test
    public void getGradebookService(){
        Iterable<CollegeStudent> collegeStudentIterable = studentService.getGradebook();
        List<CollegeStudent> students = new ArrayList<>();

        for(CollegeStudent student : collegeStudentIterable){
            students.add(student);
        }

        assertEquals(6, students.size());
    }

    @Test
    public void createGradeService(){
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));

        assertTrue(studentService.checkIfStudentIsNull(1), "Student is null");

        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

        assertEquals(2, ((Collection<MathGrade>) mathGrades).size());
        assertEquals(2, ((Collection<ScienceGrade>) scienceGrades).size());
        assertEquals(2, ((Collection<HistoryGrade>) historyGrades).size());
    }

    @Test
    public void testFailCreateGradeService(){
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-1, 1, "math"));
        assertFalse(studentService.createGrade(11, 2, "math"));
        assertFalse(studentService.createGrade(11, 1, "invalidtype"));
    }

    @Test
    public void testDeleteGrade(){
        assertEquals(1, studentService.deleteGrade(1, "math"), "Returns student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "science"), "Returns student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "history"), "Returns student id after delete");
    }
}
