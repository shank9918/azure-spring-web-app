package com.shashankg.azure.cert.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "No result found for this request.")
public class NoResultException extends RuntimeException {

	private static final long serialVersionUID = 3456062857444564292L;

	public NoResultException(String message) {
		super(message);
	}
}
