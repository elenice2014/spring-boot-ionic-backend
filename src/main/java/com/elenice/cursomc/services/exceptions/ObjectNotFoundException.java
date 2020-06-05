package com.elenice.cursomc.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	//exception da RuntimeException
	public ObjectNotFoundException(String msg) {

		super(msg);
	}
	
	//exception da RuntimeException + a causa 
	public ObjectNotFoundException(String msg, Throwable cause) {

		super(msg, cause);
	}

}
