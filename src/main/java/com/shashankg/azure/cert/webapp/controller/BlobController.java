package com.shashankg.azure.cert.webapp.controller;

import com.shashankg.azure.cert.webapp.exceptions.BlobCreationException;
import com.shashankg.azure.cert.webapp.service.AzureStorageBlobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BlobController {

	private final AzureStorageBlobService azureStorageBlobService;

	@Autowired
	public BlobController(AzureStorageBlobService azureStorageBlobService) {
		this.azureStorageBlobService = azureStorageBlobService;
	}

	@PostMapping(value = "/blob", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String uploadBlob() {
		try {
			this.azureStorageBlobService.uploadBlob();
			return "Uploaded Successfully.";
		} catch (BlobCreationException e) {
			log.error(e.getLocalizedMessage());
			return "Blob upload failed";
		}
	}
}
