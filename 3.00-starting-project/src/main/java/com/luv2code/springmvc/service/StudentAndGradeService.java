package com.luv2code.springmvc.service;


import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradeDao scienceGradeDao;

    @Autowired
    private HistoryGradeDao historyGradeDao;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    public void createStudent(String fName, String lName, String emailAddress){
        CollegeStudent student = new CollegeStudent(fName, lName, emailAddress);
        student.setId(0);
        studentDao.save(student);

    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);

        if(student.isPresent()){
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteStudent(int id) {

        if(checkIfStudentIsNull(id)){
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradeDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> students = studentDao.findAll();

        return students;
    }

    public boolean createGrade(double grade, int studentId, String gradeType){

        if(!checkIfStudentIsNull(studentId)){
            return false;
        }

        if(grade >= 0 && grade <= 100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
                return true;
            }

            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDao.save(scienceGrade);
                return true;
            }

            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int id, String type){
        int studentId = 0;

        if(type.equals("math")){
            Optional<MathGrade> grade = mathGradeDao.findById(id);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDao.deleteById(id);
        }
        if(type.equals("science")){
            Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDao.deleteById(id);
        }
        if(type.equals("history")){
            Optional<HistoryGrade> grade = historyGradeDao.findById(id);
            if(!grade.isPresent()){
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradeDao.deleteById(id);
        }
        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id) {

        if(!checkIfStudentIsNull(id)) {
            return null;
        }
        Optional<CollegeStudent> student = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

        List<Grade> mathGradesList = new ArrayList<>();
        mathGrades.forEach(mathGradesList::add);

        List<Grade> scienceGradesList = new ArrayList<>();
        scienceGrades.forEach(scienceGradesList::add);

        List<Grade> historyGradesList = new ArrayList<>();
        historyGrades.forEach(historyGradesList::add);

        studentGrades.setMathGradeResults(mathGradesList);
        studentGrades.setScienceGradeResults(scienceGradesList);
        studentGrades.setHistoryGradeResults(historyGradesList);

        return new GradebookCollegeStudent(student.get().getId(),
                student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(), studentGrades);
    }
}
