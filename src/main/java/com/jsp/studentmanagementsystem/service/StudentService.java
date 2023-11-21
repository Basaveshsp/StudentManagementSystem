package com.jsp.studentmanagementsystem.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.utility.ResponseStructure;

public interface StudentService {

	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) ;
	
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest,int studentId) ;
	
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(int studentId) ;
	
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent(int studentId) ;
	
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() ;
	
	public ResponseEntity<ResponseStructure<StudentResponse>> FindByEmail(String email);
	
	public ResponseEntity<ResponseStructure<List<String>>> findAllEmailsByGrade(String grade) ;
		
 
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(MultipartFile file) throws IOException ;
	
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException ;
	
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> readFromCSVFile(MultipartFile file) throws IOException;
	
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> writeToCSVFile(String filePath);
	
	public ResponseEntity<String> sendMail(MessageData messageData);
	public ResponseEntity<String> sendMimeMail(MessageData messageData) throws MessagingException ;
}
