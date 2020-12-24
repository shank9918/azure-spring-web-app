package com.shashankg.azure.cert.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todo {

	private String id;
	private String todoItem;
	private Status status;
}

enum Status {
	SCHEDULED,
	IN_PROGRESS,
	COMPLETED
}
