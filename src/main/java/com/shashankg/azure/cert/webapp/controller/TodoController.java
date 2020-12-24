package com.shashankg.azure.cert.webapp.controller;

import com.shashankg.azure.cert.webapp.exceptions.NoResultException;
import com.shashankg.azure.cert.webapp.model.Todo;
import com.shashankg.azure.cert.webapp.model.User;
import com.shashankg.azure.cert.webapp.service.AzureCosmosDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class TodoController {

	private final AzureCosmosDbService azureCosmosDbService;

	@Autowired
	public TodoController(AzureCosmosDbService azureCosmosDbService) {
		this.azureCosmosDbService = azureCosmosDbService;
	}

	@PostMapping(value = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addTodo(@RequestBody Todo todo, @RequestAttribute User user) {
		todo.setId(UUID.randomUUID().toString());
		todo.setUser(user);
		this.azureCosmosDbService.addTodo(todo);
		log.info("Created {}.", todo);
	}

	@GetMapping(value = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Todo>> fetchTodo(@RequestAttribute User user) {
		log.info("Fetching todo.");
		return ResponseEntity.ok(this.azureCosmosDbService.fetchTodo(user));
	}

	@GetMapping(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Todo> fetchTodoById(@PathVariable String id) {
		log.info("Fetching todo {}", id);
		List<Todo> todos = this.azureCosmosDbService.fetchTodoById(id);
		if (!todos.isEmpty()) {
			return ResponseEntity.ok(todos.get(0));
		}
		throw new NoResultException("No entry found for " + id);
	}

	@DeleteMapping(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteTodo(@PathVariable String id) {
		log.info("Deleting todo {}", id);
		this.azureCosmosDbService.deleteTodo(id);
	}

	@PutMapping(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void updateTodo(@PathVariable String id, @RequestBody Todo todo) {
		log.info("Updating todo {}", id);
		this.azureCosmosDbService.updateTodo(id, todo);
	}
}
