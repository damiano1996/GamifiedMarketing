package com.derin.damiano.exceptions;

public class EmptyQuestionnaireException extends Exception {

	public EmptyQuestionnaireException() {
		super("One or more questions were empty.");
	}

}
