package com.shashankg.azure.cert.webapp.service;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import com.microsoft.aad.msal4j.MsalException;
import com.microsoft.aad.msal4j.SilentParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Collections;

@Slf4j
@Service
public class AzureActiveDirectoryTokenService {

	private final String clientId;
	private final String clientSecret;
	private final String resource;
	private final String authority;

	public AzureActiveDirectoryTokenService(@Value("${azure.web-app.client-id}") String clientId,
	                                        @Value("${azure.web-app.client-secret}") String clientSecret,
	                                        @Value("${azure.function.todo.resource}") String resource,
	                                        @Value("${azure.web-app.authority}") String authority) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.resource = resource;
		this.authority = authority;
	}

	public IAuthenticationResult acquireTokenForTodoFunction() throws MalformedURLException {
		// Load token cache from file and initialize token cache aspect. The token cache will have
		// dummy data, so the acquireTokenSilently call will fail.

		IClientCredential credential = ClientCredentialFactory.createFromSecret(clientSecret);
		ConfidentialClientApplication confidentialClientApplication =
				ConfidentialClientApplication
						.builder(clientId, credential)
						.authority(authority)
						.build();

		IAuthenticationResult result;
		try {
			SilentParameters silentParameters = SilentParameters.builder(Collections.singleton(resource)).build();
			// try to acquire token silently. This call will fail since the token cache does not
			// have a token for the application you are requesting an access token for
			result = confidentialClientApplication.acquireTokenSilently(silentParameters).join();
		} catch (Exception exception) {
			if (exception.getCause() instanceof MsalException) {
				ClientCredentialParameters parameters = ClientCredentialParameters.builder(Collections.singleton(resource)).build();
				// Try to acquire a token. If successful, you should see
				// the token information printed out to console
				result = confidentialClientApplication.acquireToken(parameters).join();
			} else {
				throw exception;
			}
		}
		return result;
	}
}
