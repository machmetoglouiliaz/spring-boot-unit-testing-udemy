package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

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

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    StudentDao studentDao;

    @Mock
    private StudentAndGradeService studentService;

    @BeforeAll
    public static void setup(){
        request = new MockHttpServletRequest();
        request.addParameter("firstname", "Mourat");
        request.addParameter("lastname", "Achmet");
        request.addParameter("email", "m@g.com");
    }

    @BeforeEach
    public void beforeEach() {

        jdbc.execute(createStudentScript);
        jdbc.execute(createMathScript);
        jdbc.execute(createScienceScript);
        jdbc.execute(createHistoryScript);
    }

    @AfterEach
    public void afterEach() {

        jdbc.execute(deleteStudentScript);
        jdbc.execute(deleteMathScript);
        jdbc.execute(deleteScienceScript);
        jdbc.execute(deleteHistoryScript);
    }

    @Test
    public void getStudentsHttpRequest() throws Exception {

        CollegeStudent student1 = new CollegeStudent("Mourat", "Achmet", "m@g.com");
        CollegeStudent student2 = new CollegeStudent("Mourat", "Achmet2", "m2@g.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(student1, student2));

        when(studentService.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentService.getGradebook());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    public void createStudentHttpRequest() throws Exception{

        CollegeStudent student1 = new CollegeStudent("Mourat", "Achmet", "m@g.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(student1));

        when(studentService.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentService.getGradebook());

        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("email", request.getParameterValues("email")))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent searchedStudent = studentDao.findByEmailAddress("m@g.com");

        assertNotNull(searchedStudent, "The student should not be null");
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception{

        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception{

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void studentInformationHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "studentInformation");
    }

    @Test
    public void studentInformationHttpRequestNotExist() throws Exception {
        assertFalse(studentDao.findById(0).isPresent());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

}
