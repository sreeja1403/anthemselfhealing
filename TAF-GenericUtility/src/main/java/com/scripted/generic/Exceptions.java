package com.scripted.generic;

public class Exceptions extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Exceptions(RuntimeException t) {
		super(t);
	}

	public Exceptions(String message) {
		super(message);
	}

	public Exceptions(String message, RuntimeException e) {
		super(message, e);
	}
}
