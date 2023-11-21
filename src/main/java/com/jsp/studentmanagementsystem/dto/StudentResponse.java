package com.jsp.studentmanagementsystem.dto;

public class StudentResponse {

	private int studentId;
	private String studentName;
	private String studentGrade;
	private int getStudentId() {
		return studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentGrade() {
		return studentGrade;
	}
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	

	
	
}
