package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

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

        jdbc.execute("INSERT INTO student(firstname, lastname, email_address) " + "VALUES ('Mourat', 'Achmet', 'm@g.com')");
    }

    @AfterEach
    public void afterEach() {

        jdbc.execute("DELETE FROM student");

        jdbc.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
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
        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("email", request.getParameterValues("email")))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
    }


}
