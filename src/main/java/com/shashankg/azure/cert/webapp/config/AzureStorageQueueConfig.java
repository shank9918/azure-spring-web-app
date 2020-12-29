package com.shashankg.azure.cert.webapp.config;

import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.QueueServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.shashankg.azure.cert.webapp.constants.AzureConstants.QUEUE_URL;

@Slf4j
@Configuration
public class AzureStorageQueueConfig {

	private final String queueServiceUrl;
	private final String token;

	public AzureStorageQueueConfig(@Value("${azure-storage-account-name}") String storageAccountName,
	                               @Value("${azure-storage-account-token}") String token) {
		this.queueServiceUrl = "http://" + storageAccountName + QUEUE_URL;
		this.token = token;
	}

	@Bean
	public QueueServiceClient queueServiceClient() {
		log.debug("Storage Account Url: {}, Token: {}", this.queueServiceUrl, this.token);
		return new QueueServiceClientBuilder()
				.endpoint(this.queueServiceUrl)
				.sasToken(this.token)
				.buildClient();
	}

}
