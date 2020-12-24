package com.shashankg.azure.cert.webapp.controller;

import com.shashankg.azure.cert.webapp.model.QueueItem;
import com.shashankg.azure.cert.webapp.service.AzureStorageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueueController {

	private final AzureStorageQueueService azureStorageQueueService;

	@Autowired
	public QueueController(AzureStorageQueueService azureStorageQueueService) {
		this.azureStorageQueueService = azureStorageQueueService;
	}

	@GetMapping(value = "/queue", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> listQueues() {
		return this.azureStorageQueueService.listQueue();
	}

	@PostMapping(value = "/queue", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String enqueue(@RequestBody QueueItem queueItem) {
		this.azureStorageQueueService.enqueue(queueItem.getQueue(), queueItem.getMessage());
		return "Created";
	}

	@GetMapping(value = "/queue/{queueName}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> dequeue(@PathVariable String queueName) {
		return this.azureStorageQueueService.dequeue(queueName);
	}
}
