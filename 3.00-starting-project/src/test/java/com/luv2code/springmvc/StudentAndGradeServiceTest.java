package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Value("${sql.student.create}")
    private String createStudentScript;

    @Value("${sql.mathgrade.create}")
    private String createMathScript;

    @Value("${sql.sciencegrade.create}")
    private String createScienceScript;

    @Value("${sql.historygrade.create}")
    private String createHistoryScript;

    @Value("${sql.student.delete}")
    private String deleteStudentScript;

    @Value("${sql.mathgrade.delete}")
    private String deleteMathScript;

    @Value("${sql.sciencegrade.delete}")
    private String deleteScienceScript;

    @Value("${sql.historygrade.delete}")
    private String deleteHistoryScript;

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

        jdbc.execute(createStudentScript);
        jdbc.execute(createMathScript);
        jdbc.execute(createScienceScript);
        jdbc.execute(createHistoryScript);
    }

    @AfterEach
    public void afterEach(){

        jdbc.execute(deleteStudentScript);
        jdbc.execute(deleteMathScript);
        jdbc.execute(deleteScienceScript);
        jdbc.execute(deleteHistoryScript);

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
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);

        assertTrue(student.isPresent(), "Didnt find student with id 1");
        assertTrue(deletedMathGrade.isPresent(), "Didnt find math grade with id 1");
        assertTrue(deletedScienceGrade.isPresent(), "Didnt find science grade with id 1");
        assertTrue(deletedHistoryGrade.isPresent(), "Didnt find history grade with id 1");

        studentService.deleteStudent(1);
        student = studentDao.findById(1);
        deletedMathGrade = mathGradeDao.findById(1);
        deletedScienceGrade = scienceGradeDao.findById(1);
        deletedHistoryGrade = historyGradeDao.findById(1);

        assertFalse(student.isPresent(), "Shouldn't find a student with id 1");
        assertFalse(deletedMathGrade.isPresent(), "Shouldn't find math grade with id 1");
        assertFalse(deletedScienceGrade.isPresent(), "Shouldn't find science grade with id 1");
        assertFalse(deletedHistoryGrade.isPresent(), "Shouldn't find history grade with id 1");
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

    @Test
    public void deleteGradeServiceReturnStudentIdOfZero(){
        assertEquals(0, studentService.deleteGrade(0, "science"), "No student should have id 0");
        assertEquals(0, studentService.deleteGrade(1, "notype"), "There is no notype class");
    }

    @Test
    public void studentInformation() {

        GradebookCollegeStudent student = studentService.studentInformation(1);

        assertNotNull(student);
        assertEquals(1, student.getId());
        assertEquals("Mourat", student.getFirstname());
        assertEquals("Achmet", student.getLastname());
        assertEquals("m@g.com", student.getEmailAddress());
        assertEquals(1, student.getStudentGrades().getMathGradeResults().size());
        assertEquals(1, student.getStudentGrades().getScienceGradeResults().size());
        assertEquals(1, student.getStudentGrades().getHistoryGradeResults().size());

    }

    @Test
    public void studentInformationServiceReturnNull() {
        GradebookCollegeStudent student = studentService.studentInformation(0);

        assertNull(student);
    }
}
