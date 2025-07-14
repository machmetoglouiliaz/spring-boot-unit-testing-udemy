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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @MockitoBean
    private ApplicationDao appDao;

    @Autowired
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

    @DisplayName("Find GPA")
    @Test
    public void testFindGpa(){
        when(appDao.findGradePointAverage(grades.getMathGradeResults())).thenReturn(88.31);

        assertEquals(88.31, appService.findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Not Null")
    @Test
    public void testNotNull(){
        when(appDao.checkNull(grades.getMathGradeResults())).thenReturn(true);

        assertNotNull(appService.checkNull(student.getStudentGrades().getMathGradeResults()), "Object should not be null");
    }

    @DisplayName("Throw runtime exception")
    @Test
    public void testThrowRuntimeError(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(appDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class,
            () -> {
                appService.checkNull(nullStudent);
            });

        verify(appDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    public void stubbingConsecutiveCalls(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(appDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, () -> {
            appService.checkNull(nullStudent);
        });

        assertEquals("Do not throw exception second time", appService.checkNull(nullStudent));

        verify(appDao, times(2)).checkNull(nullStudent);
    }
}
