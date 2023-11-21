package com.jsp.studentmanagementsystem.exception;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jsp.studentmanagementsystem.utility.ErrorStructure;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFound(StudentNotFoundByIdException ex) {

		ErrorStructure structure = new ErrorStructure();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not present with requested id");
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFoundByMail(StudentNotFoundByMail e) {
		
		ErrorStructure structure = new ErrorStructure();
		structure.setMessage(e.getMessage());
		structure.setRootCause("Student Not found in Database");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorStructure>(structure,HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		
		HashMap<String, String>errors= new HashMap<String, String>();
		for (ObjectError objectError : allErrors) {
			FieldError fieldError=(FieldError)objectError;
			String defaultMessage = fieldError.getDefaultMessage();
			String field = fieldError.getField();
			
			errors.put(field, defaultMessage);
		}
		
		
		return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
	}
	
	
}
