package com.shashankg.azure.cert.webapp.service;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AzureStorageQueueService {

	private final QueueServiceClient queueServiceClient;

	@Autowired
	public AzureStorageQueueService(QueueServiceClient queueServiceClient) {
		this.queueServiceClient = queueServiceClient;
	}

	public List<String> listQueue() {
		final List<String> queueList = new ArrayList<>();
		this.queueServiceClient.listQueues().forEach(queueItem -> queueList.add(queueItem.getName()));
		return queueList;
	}

	public void enqueue(String queueName, String message) {
		log.info("Enqueuing {} into {}", message, queueName);
		QueueClient queueClient = this.queueServiceClient.getQueueClient(queueName);
		queueClient.sendMessage(message);
	}

	public List<String> dequeue(String queueName) {
		log.info("Peeking message from {}", queueName);
		List<String> messages = new ArrayList<>();
		QueueClient queueClient = this.queueServiceClient.getQueueClient(queueName);
		queueClient.receiveMessages(1).forEach(queueMessageItem -> messages.add(queueMessageItem.getMessageText()));
		return messages;
	}
}
