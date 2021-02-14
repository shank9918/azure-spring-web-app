package com.shashankg.azure.cert.webapp.service;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.shashankg.azure.cert.webapp.model.Todo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

@Slf4j
@Service
public class AzureFunctionService {

	private final AzureActiveDirectoryTokenService azureActiveDirectoryTokenService;
	private final URI functionUri;

	public AzureFunctionService(AzureActiveDirectoryTokenService azureActiveDirectoryTokenService, @Value("${azure.function.todo.uri}") String functionUri) {
		this.azureActiveDirectoryTokenService = azureActiveDirectoryTokenService;
		this.functionUri = URI.create(functionUri);
	}

	public void callFunctionApp(Todo todo) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(functionUri);
			httpPost.setEntity(new StringEntity(todo.toString()));
			httpPost.addHeader("Authorization", "Bearer " + getAccessToken());
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = client.execute(httpPost);
			log.info("Response Code: {}", response.getStatusLine().getStatusCode());
			log.info("Response Message: {}", response.getStatusLine().getReasonPhrase());
		} catch (IOException e) {
			log.error("IOException", e);
		}
	}

	private String getAccessToken() throws MalformedURLException {
		log.info("Acquiring Access Token");
		IAuthenticationResult iAuthenticationResult = azureActiveDirectoryTokenService.acquireTokenForTodoFunction();
		String accessToken = iAuthenticationResult.accessToken();
		log.info("Access Token: {}", accessToken);
		return accessToken;
	}
}
