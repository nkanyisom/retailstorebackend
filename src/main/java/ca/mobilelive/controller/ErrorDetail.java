package ca.mobilelive.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorDetail {

	private String detail;

	private String developerMessage;
	private Map<String, List<ValidationError>> errors = new HashMap<>();
	private int status;
	private long timeStamp;
	private String title;
	public ErrorDetail() {
		super();

	}

}
