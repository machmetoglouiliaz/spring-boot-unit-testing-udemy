package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> collegeStudents = studentService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}

	@PostMapping(value = "/")
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m){
		studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
		Iterable<CollegeStudent> collegeStudents = studentService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable("id") int id, Model m){

		if(!studentService.checkIfStudentIsNull(id)) {
			return "error";
		}
		studentService.deleteStudent(id);
		Iterable<CollegeStudent> students = studentService.getGradebook();
		m.addAttribute("students", students);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {

		if(!studentService.checkIfStudentIsNull(id)){
			return "error";
		}

		GradebookCollegeStudent studentEntity = studentService.studentInformation(id);

		m.addAttribute("student", studentEntity);
		if(!studentEntity.getStudentGrades().getMathGradeResults().isEmpty()) {
			m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getMathGradeResults()
			));
		} else {
			m.addAttribute("mathAverage", "N/A");
		}

		if(!studentEntity.getStudentGrades().getScienceGradeResults().isEmpty()) {
			m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getScienceGradeResults()
			));
		} else {
			m.addAttribute("scienceAverage", "N/A");
		}

		if(!studentEntity.getStudentGrades().getHistoryGradeResults().isEmpty()) {
			m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getHistoryGradeResults()
			));
		} else {
			m.addAttribute("historyAverage", "N/A");
		}
		return "studentInformation";
	}

}
