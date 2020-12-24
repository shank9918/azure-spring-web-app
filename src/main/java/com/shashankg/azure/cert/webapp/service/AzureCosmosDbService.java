package com.shashankg.azure.cert.webapp.service;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.shashankg.azure.cert.webapp.model.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AzureCosmosDbService {

	private final CosmosContainer cosmosContainer;

	@Autowired
	public AzureCosmosDbService(CosmosContainer cosmosContainer) {
		this.cosmosContainer = cosmosContainer;
	}

	public void addTodo(Todo todo) {
		CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions();
		CosmosItemResponse<Todo> cosmosItemResponse = cosmosContainer.createItem(todo, new PartitionKey(todo.getId()), cosmosItemRequestOptions);
		log.info("Created {} in {} duration.", cosmosItemResponse.getItem(), cosmosItemResponse.getDuration());
	}

	public List<Todo> fetchTodo() {
		List<Todo> todos = new ArrayList<>();
		CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
		queryOptions.setQueryMetricsEnabled(true);

		CosmosPagedIterable<Todo> usersPagedIterable = cosmosContainer.queryItems(
				"SELECT * FROM Todo", queryOptions, Todo.class);
		usersPagedIterable.iterableByPage(10).forEach(userFeedResponse -> {
			log.info("Got a page of query result with {} items(s) and request charge of {}",
					userFeedResponse.getResults().size(), userFeedResponse.getRequestCharge());
			todos.addAll(userFeedResponse.getResults());
		});
		return todos;
	}

	public List<Todo> fetchTodoById(String id) {
		List<Todo> todos = new ArrayList<>();
		CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
		queryOptions.setQueryMetricsEnabled(true);

		CosmosPagedIterable<Todo> usersPagedIterable = cosmosContainer.queryItems(
				"SELECT * FROM Todo where Todo.id='" + id + '\'', queryOptions, Todo.class);
		usersPagedIterable.iterableByPage(10).forEach(userFeedResponse -> {
			log.info("Got a page of query result with {} items(s) and request charge of {}",
					userFeedResponse.getResults().size(), userFeedResponse.getRequestCharge());
			todos.addAll(userFeedResponse.getResults());
		});
		return todos;
	}

	public void deleteTodo(String id) {
		CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions();
		cosmosContainer.deleteItem(id, new PartitionKey(id), cosmosItemRequestOptions);
	}

	public void updateTodo(String id, Todo todo) {
		CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions();
		cosmosContainer.replaceItem(todo, id, new PartitionKey(id), cosmosItemRequestOptions);
	}
}
