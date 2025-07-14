package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @Mock
    private ApplicationDao appDao;

    @InjectMocks
    private ApplicationService appService;

    @BeforeEach
    public void beforeEach(){
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("er@g.com");
        student.setStudentGrades(grades);
    }

    @DisplayName("When & Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        when(appDao.addGradeResultsForSingleClass(grades.getMathGradeResults())).thenReturn(100.0);

        assertEquals(100, appService.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()));

        verify(appDao).addGradeResultsForSingleClass(grades.getMathGradeResults());
        verify(appDao, times(1)).addGradeResultsForSingleClass(grades.getMathGradeResults());
    }
}
