package com.shashankg.azure.cert.webapp.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.shashankg.azure.cert.webapp.constants.AzureConstants.BLOB_URL;

@Slf4j
@Configuration
public class AzureStorageBlobConfig {

	private final String storageAccountUrl;
	private final String token;
	private final String container;

	public AzureStorageBlobConfig(@Value("${azure-storage-account-name}") String storageAccountName,
	                              @Value("${azure-storage-account-token}") String token,
	                              @Value("${azure.storage.account.blob-container}") String container) {
		this.storageAccountUrl = "https://" + storageAccountName + BLOB_URL;
		this.token = token;
		this.container = container;
	}

	@Bean
	public BlobContainerClient blobContainerClient() {
		log.debug("Storage Account Url: {}, Token: {}, Container: {}", this.storageAccountUrl, this.token, this.container);
		return new BlobContainerClientBuilder()
				.endpoint(this.storageAccountUrl)
				.sasToken(this.token)
				.containerName(this.container)
				.buildClient();
	}
}
