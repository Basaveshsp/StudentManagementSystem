package com.jsp.studentmanagementsystem.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collector;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.studentmanagementsystem.dto.MessageData;
import com.jsp.studentmanagementsystem.dto.StudentRequest;
import com.jsp.studentmanagementsystem.dto.StudentResponse;
import com.jsp.studentmanagementsystem.entity.Student;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByIdException;
import com.jsp.studentmanagementsystem.exception.StudentNotFoundByMail;
import com.jsp.studentmanagementsystem.exception.StudentsEmailNotFoundByGrade;
import com.jsp.studentmanagementsystem.repo.StudentRepo;
import com.jsp.studentmanagementsystem.service.StudentService;
import com.jsp.studentmanagementsystem.utility.ResponseStructure;

//mail password-tszorjhsademuesd
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo repo;

	@Autowired
	private JavaMailSender javaMailSender;
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {

		Student student = new Student();
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPhNo(studentRequest.getStudentPhNo());
		student.setStudentPassword(studentRequest.getStudentPassword());
		Student student2 = repo.save(student);
		
		StudentResponse studentResponse = new StudentResponse();
		studentResponse.setStudentId(student.getStudentId());
		studentResponse.setStudentGrade(student.getStudentGrade());
		studentResponse.setStudentName(student.getStudentName());
		ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
		structure.setData(studentResponse);
		structure.setMessage(student2.getStudentName() + " Student Data saved  to DB Sucessfully");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	// Create one student Object and Add all data and Save 
	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, int studentId) {
		Optional<Student> optional = repo.findById(studentId);
	
		if (optional.isPresent()) {
			
			Student student= new Student();
			student.setStudentId(studentId);
			student.setStudentName(studentRequest.getStudentName());
			student.setStudentEmail(studentRequest.getStudentEmail());
			student.setStudentPhNo(studentRequest.getStudentPhNo());
			student.setStudentGrade(studentRequest.getStudentGrade());
			
			//Save the entity
			Student student3 = repo.save(student);
			
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(student3.getStudentId());
			studentResponse.setStudentName(student3.getStudentName());
			 studentResponse.setStudentGrade(student3.getStudentGrade());
			 //
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setData(studentResponse);
			structure.setMessage("Data Updated Succesfully");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);

		} else {
			throw new StudentNotFoundByIdException("Failed to update the student!!!!");
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> deleteStudent(int studentId) {

		Optional<Student> optional = repo.findById(studentId);

		if (optional.isPresent()) {
			Student student = optional.get();
			repo.delete(student);
			
			StudentResponse response = new StudentResponse();
			response.setStudentGrade(student.getStudentGrade());
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			
			ResponseStructure<StudentResponse>structure = new ResponseStructure<StudentResponse>();
			structure.setData(response);
			structure.setMessage("Data deleted Successfully");
			structure.setStatusCode(HttpStatus.OK.value());
			
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		} else {
			throw new StudentNotFoundByIdException("Failed to delete the student!!!!");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findStudent(int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		if (optional.isPresent()) {
			Student student = optional.get();
			StudentResponse studentResponse = new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentGrade(student.getStudentGrade());
			studentResponse.setStudentName(student.getStudentName());
			ResponseStructure<StudentResponse>structure = new ResponseStructure<StudentResponse>();
			structure.setData(studentResponse);
			structure.setMessage("Data found");
			structure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		} else {
			throw new StudentNotFoundByIdException("Failed to find the student!!!!");		}

	}

	//use loop to store in list of StudentResponse
	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> findAllStudents() {
		List<Student> students = repo.findAll();
		Collections.sort(students, (s1,s2)->s1.getStudentName().compareTo(s2.getStudentName()));
		List<StudentResponse>studentResponses = new ArrayList<StudentResponse>();
		for (Student student : students) {
			StudentResponse studentResponse= new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			studentResponses.add(studentResponse);
		}
		ResponseStructure<List<StudentResponse>> structure= new ResponseStructure<List<StudentResponse>>();
		structure.setData(studentResponses);
		structure.setMessage("All Student Data found");
		structure.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> FindByEmail(String email) {
		Student student = repo.findByStudentEmail(email);
		
		if (student!=null) {
			StudentResponse studentResponse= new StudentResponse();
			studentResponse.setStudentId(student.getStudentId());
			studentResponse.setStudentName(student.getStudentName());
			studentResponse.setStudentGrade(student.getStudentGrade());
			ResponseStructure<StudentResponse>structure = new ResponseStructure<StudentResponse>();
					structure.setData(studentResponse);
			structure.setMessage("Student Found Successfully");
			structure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure,HttpStatus.FOUND);
		} else {
			throw new StudentNotFoundByMail("student with this mail not exist ");
			
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<List<String>>> findAllEmailsByGrade(String grade) {
		List<String> emails = repo.getAllEmailsByGrade(grade);
		
		if (!emails.isEmpty()) {
			ResponseStructure<List<String>> structure = new ResponseStructure<List<String>>();
			structure.setData(emails);
			structure.setMessage("All emails fetched");
			structure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<List<String>>>(structure,HttpStatus.FOUND);
		} else {
		
			throw new StudentsEmailNotFoundByGrade("Student with this Grade Not Exist");
			
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> extractDataFromExcel(MultipartFile file) throws IOException {
	
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
	
		int l = workbook.getNumberOfSheets();
		System.out.println(l);
		
		ArrayList<StudentResponse>list= new ArrayList<StudentResponse>();
		for (Sheet sheet : workbook) {
			for (Row row : sheet) {
				if (row.getRowNum()>0) {
					if (row!=null) {
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
					long phoneNumber=(long)row.getCell(2).getNumericCellValue();
					String grade = row.getCell(3).getStringCellValue();
					String password = row.getCell(4).getStringCellValue();
					Student student= new Student();
					student.setStudentName(name);
					student.setStudentEmail(email);
					student.setStudentGrade(grade);
					student.setStudentPassword(password);
					student.setStudentPhNo(phoneNumber);
//					Student save = repo.save(student);
//					StudentResponse studentResponse = new StudentResponse();
//					studentResponse.setStudentId(save.getStudentId());
//					studentResponse.setStudentName(save.getStudentName());
//					studentResponse.setStudentGrade(save.getStudentGrade());
//					list.add(studentResponse);
					System.out.println(name+" "+email+" "+phoneNumber+" "+grade+" "+password);
					}
				}
			}
		}
		ResponseStructure<List<StudentResponse>>structure = new ResponseStructure<List<StudentResponse>>();
		structure.setData(list);structure.setMessage("All Data saved Succesfully");
		structure.setStatusCode(HttpStatus.CREATED.value());
		workbook.close();
		return new  ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException {
	List<Student> students = repo.findAll();
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet= workbook.createSheet();
	Row header = sheet.createRow(0);
	header.createCell(0).setCellValue("StudentId");
	header.createCell(1).setCellValue("StudentEmail");
	header.createCell(2).setCellValue("StudentGrade");
	header.createCell(3).setCellValue("StudentName");
	header.createCell(4).setCellValue("StudentPassword");
	header.createCell(5).setCellValue("StudentPhoneNo");
	
	
	int rowNum=1;
	for (Student student : students) {
		Row row = sheet.createRow(rowNum++);
		row.createCell(0).setCellValue(student.getStudentId());
		row.createCell(1).setCellValue(student.getStudentEmail());
		row.createCell(2).setCellValue(student.getStudentGrade());
		row.createCell(3).setCellValue(student.getStudentName());
		row.createCell(4).setCellValue(student.getStudentPassword());
		row.createCell(5).setCellValue(student.getStudentPhNo());
		
	}
	
	
	
	FileOutputStream outputStream = new FileOutputStream(filePath);
	workbook.write(outputStream);
	workbook.close();
		return new ResponseEntity<String>("Data transferred to excel",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> readFromCSVFile(MultipartFile file)
			throws IOException {
		Reader reader = new InputStreamReader(file.getInputStream());
		char delimiter='#';
		CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(delimiter));
		Spliterator<CSVRecord> spliterator = parser.spliterator();
		
		List<CSVRecord> records = parser.getRecords();
		ArrayList<StudentResponse> list = new ArrayList<StudentResponse>();

		for (CSVRecord csvRecord : records) {
			long recordNumber = csvRecord.getRecordNumber();

			if (recordNumber > 1) {
				String email = csvRecord.get(0);
				String grade = csvRecord.get(1);
				String name = csvRecord.get(2);
				String password = csvRecord.get(3);
				long phNo = Long.parseLong(csvRecord.get(4));
				System.out.println(email + " " + grade + " " + name + " " + password + " " + phNo);
				Student student = new Student();
				student.setStudentEmail(email);
				student.setStudentGrade(grade);
				student.setStudentName(name);
				student.setStudentPassword(password);
				student.setStudentPhNo(phNo);

//				Student student2 = repo.save(student);
//				StudentResponse studentResponse = new StudentResponse();
//				studentResponse.setStudentGrade(student2.getStudentGrade());
//				studentResponse.setStudentId(student2.getStudentId());
//				studentResponse.setStudentName(student2.getStudentName());
//				list.add(studentResponse);
			}
		}
		ResponseStructure<List<StudentResponse>> structure = new ResponseStructure<List<StudentResponse>>();
		structure.setData(list);
		structure.setMessage("Data saved Succesfully from CSV file");
		structure.setStatusCode(HttpStatus.CREATED.value());
		reader.close();
		return new ResponseEntity<ResponseStructure<List<StudentResponse>>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> writeToCSVFile(String filePath) {
		 List<Student> students = repo.findAll(); 
		 
		 
		
		return null;
	}
	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText()+"\n\n Thanks and Regards "+"\n"+messageData.getSenderName()+"\n"+messageData.getSenderAddress());
		message.setSentDate( new Date());
		
		javaMailSender.send(message);
		return new ResponseEntity<String>("mail sent successfully ",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMimeMail(MessageData messageData) throws MessagingException {
	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
	
	message.setTo(messageData.getTo());
	message.setSubject(messageData.getSubject());
	String emailBody=messageData.getText()+"<br><br><h4>Thanks &regards </h4><h4>"+messageData.getSenderName()
											+"</h4><h4>"+messageData.getSenderAddress()+"</h4>"+ "<img src=\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"250\">";
	

	message.setText(emailBody, true);
	message.setSentDate(new Date());
	javaMailSender.send(mimeMessage);
	return new  ResponseEntity<String>("MimeMail sent successfully",HttpStatus.OK);
	}

	
	
	
}
