package com.wildboar.web.rest;

import java.io.Serializable;

public class StudentMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String studentId;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "Student{'studentId='" + getStudentId() + "'" + "}";
	}
}
