package com.shashankg.azure.cert.webapp.controller;

import com.shashankg.azure.cert.webapp.model.Todo;
import com.shashankg.azure.cert.webapp.service.AzureCosmosDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class TodoController {

	private final AzureCosmosDbService azureCosmosDbService;

	@Autowired
	public TodoController(AzureCosmosDbService azureCosmosDbService) {
		this.azureCosmosDbService = azureCosmosDbService;
	}

	@PostMapping(value = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> addTodo(@RequestBody Todo todo) {
		todo.setId(UUID.randomUUID().toString());
		this.azureCosmosDbService.addTodo(todo);
		log.info("Created {}.", todo);
		return ResponseEntity.ok("Created");
	}

	@GetMapping(value = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Todo>> fetchTodo() {
		log.info("Fetching todo.");
		return ResponseEntity.ok(this.azureCosmosDbService.fetchTodo());
	}

	@GetMapping(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Todo> fetchTodoById(@PathVariable String id) {
		log.info("Fetching todo {}", id);
		return ResponseEntity.ok(this.azureCosmosDbService.fetchTodoById(id).get(0));
	}
}
