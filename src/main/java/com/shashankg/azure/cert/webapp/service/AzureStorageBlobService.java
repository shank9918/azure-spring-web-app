package com.shashankg.azure.cert.webapp.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.shashankg.azure.cert.webapp.exceptions.BlobCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class AzureStorageBlobService {

	public static final String BLOB_OBJECT = "blob_object";

	private final BlobContainerClient blobContainerClient;

	@Autowired
	public AzureStorageBlobService(BlobContainerClient blobContainerClient) {
		this.blobContainerClient = blobContainerClient;
	}

	public void uploadBlob() throws BlobCreationException {
		log.info("Uploading blob to {}", this.blobContainerClient.getBlobContainerName());
		UUID uuid = UUID.randomUUID();
		BlobClient blobClient = blobContainerClient.getBlobClient(BLOB_OBJECT + uuid);
		try (ByteArrayInputStream dataStream = new ByteArrayInputStream(uuid.toString().getBytes())) {
			blobClient.getBlockBlobClient().upload(dataStream, uuid.toString().length());
		} catch (IOException e) {
			throw new BlobCreationException("Exception while uploading a blob.", e);
		}
	}
}
