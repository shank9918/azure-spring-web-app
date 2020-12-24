package com.shashankg.azure.cert.webapp.config;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class AzureCosmosDbConfig {

	private final String endpoint;
	private final String key;
	private final String database;
	private final String container;

	public AzureCosmosDbConfig(@Value("${azure.cosmos.url}") String endpoint,
	                           @Value("${azure.cosmos.key}") String key,
	                           @Value("${azure.cosmos.database}") String database,
	                           @Value("${azure.cosmos.container}") String container) {
		this.endpoint = endpoint;
		this.key = key;
		this.database = database;
		this.container = container;
	}

	@Bean
	public CosmosClient cosmosClient() {
		return new CosmosClientBuilder()
				.endpoint(this.endpoint)
				.key(this.key)
				.preferredRegions(Collections.singletonList("Central India"))
				.consistencyLevel(ConsistencyLevel.EVENTUAL)
				.buildClient();
	}

	@Bean
	public CosmosDatabase cosmosDatabase() {
		CosmosClient cosmosClient = cosmosClient();
		return cosmosClient.getDatabase(database);
	}

	@Bean
	public CosmosContainer cosmosContainer() {
		CosmosDatabase cosmosDatabase = cosmosDatabase();
		return cosmosDatabase.getContainer(container);
	}
}
