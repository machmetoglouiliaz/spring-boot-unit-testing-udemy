package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
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

    @BeforeEach
    public void beforeEach(){

        jdbc.execute("INSERT INTO student(firstname, lastname, email_address) " +
                "VALUES ('Mourat', 'Achmet', 'm@g.com')");
    }

    @AfterEach
    public void afterEach(){

        jdbc.execute("DELETE FROM student");

        jdbc.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
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

        assertTrue(studentService.checkIfStudentIsNull(1), "Student is null");

        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);

        assertTrue(mathGrades.iterator().hasNext(), "Student has math grades");
    }
}
