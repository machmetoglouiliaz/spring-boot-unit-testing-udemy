package com.luv2code.springmvc.service;


import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    StudentDao studentDao;

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
        }
    }
}
