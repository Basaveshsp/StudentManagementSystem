package com.jsp.studentmanagementsystem.exception;

public class StudentNotFoundByMail extends RuntimeException{

	private String message;

	public StudentNotFoundByMail(String message) {
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
