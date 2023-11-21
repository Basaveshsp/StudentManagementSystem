package com.jsp.studentmanagementsystem.exception;

public class StudentsEmailNotFoundByGrade extends RuntimeException {
	
	private String message;

	public StudentsEmailNotFoundByGrade(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
