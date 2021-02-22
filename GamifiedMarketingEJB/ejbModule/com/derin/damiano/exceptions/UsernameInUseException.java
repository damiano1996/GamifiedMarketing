package com.derin.damiano.exceptions;

public class UsernameInUseException extends Exception {

	public UsernameInUseException() {
		super("The username is already in use.");
	}

}
