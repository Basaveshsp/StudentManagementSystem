package com.jsp.studentmanagementsystem.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.utility.ResponseStructure;

@RestController
@RequestMapping("students")
public class StudentController {

	@Autowired
	private StudentService service;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest) {
		
		return service.saveStudent(studentRequest);
	}
	
	@PutMapping("/{stdId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody StudentRequest studentRequest,@PathVariable int stdId) {
		
		return service.updateStudent(studentRequest, stdId);
	}
	
	@DeleteMapping("{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(@PathVariable int studentId) {
		return service.deleteStudent(studentId);
	}
	
	@GetMapping("{studentId}")
	@CrossOrigin
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent( @PathVariable int studentId) {
		return service.findStudent(studentId);
		
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() {
		return service.findAllStudents();
	}	
	
	@GetMapping(params = "email")
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(@RequestParam String email) {
		return service.FindByEmail(email);
	}
	public StudentController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/grade/{grade}")
	public ResponseEntity<ResponseStructure<List<String>>> findAllEmailByGarde(@PathVariable String grade) {
		
		return service.findAllEmailsByGrade(grade);
	}
	
	@PostMapping("/extract")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractExcelFile(@RequestParam MultipartFile file) throws IOException {
		return service.extractDataFromExcel(file);
	}
	
	@GetMapping("/write/excel")
	public ResponseEntity<String> writeToExcel(@RequestParam String filePath) throws IOException {
		return service.writeToExcel(filePath);
	}
	@PostMapping("/extract/csv")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> readFromCSV(@RequestParam MultipartFile file) throws IOException {
		return service.readFromCSVFile(file);
	}
	
	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody MessageData messageData) {
		return service.sendMail(messageData);
	}
	
	@PostMapping("/mime")
	public ResponseEntity<String> sendMimeMail(@RequestBody MessageData messageData) throws MessagingException {
		return service.sendMimeMail(messageData);
	}
}
