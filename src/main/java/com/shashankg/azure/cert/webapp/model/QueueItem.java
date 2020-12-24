package com.shashankg.azure.cert.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueItem {

	private String queue;
	private String message;
}
