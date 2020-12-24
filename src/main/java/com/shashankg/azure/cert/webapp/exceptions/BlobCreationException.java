package com.shashankg.azure.cert.webapp.exceptions;

public class BlobCreationException extends Exception {

	private static final long serialVersionUID = -9056269631149836744L;

	public BlobCreationException(String message) {
		super(message);
	}

	public BlobCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
