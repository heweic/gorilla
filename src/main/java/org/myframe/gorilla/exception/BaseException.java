package org.myframe.gorilla.exception;

public class BaseException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String error;

	public BaseException() {
	}

	public BaseException(String error) {
		this.error = error;
	}

	public BaseException(String eror, Throwable cause) {
		super(eror, cause);
	}

	private static String forMat = "Gorilla Exception , because of : %s";

	@Override
	public String toString() {
		return String.format(forMat, this.error);
	}

	@Override
	public String getMessage() {
		return String.format(forMat, this.error);
	}

}
