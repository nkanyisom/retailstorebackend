package ca.mobilelive.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError {
	private String code;

	private String message;

	public ValidationError() {
		super();
	}
	public ValidationError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
