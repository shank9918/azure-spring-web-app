import {Component, OnInit} from '@angular/core';
import {AuthError, InteractionRequiredAuthError} from 'msal';
import {Todo} from '../model/todo';
// @ts-ignore
import * as config from '../app-config.json';
import {BroadcastService, MsalService} from '@azure/msal-angular';
import {TodoService} from '../todo.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-todo-view',
  templateUrl: './todo-view.component.html',
  styleUrls: ['./todo-view.component.css']
})
export class TodoViewComponent implements OnInit {

  todo: Todo;

  todos: Todo[];

  displayedColumns = ['status', 'todoItem', 'edit', 'remove'];

  private authService: MsalService;

  constructor(authService: MsalService, private service: TodoService, private broadcastService: BroadcastService) {
    this.authService = authService;
  }

  ngOnInit(): void {
    this.broadcastService.subscribe('msal:acquireTokenSuccess', (payload) => {
      console.log(payload);
      console.log('access token acquired: ' + new Date().toString());

    });

    this.broadcastService.subscribe('msal:acquireTokenFailure', (payload) => {
      console.log(payload);
      console.log('access token acquisition fails');
    });

    this.getTodos();
  }

  getTodos(): void {
    console.log('Getting List');
    this.service.getTodos().subscribe({
      next: (response: Todo[]) => {
        this.todos = response;
      },
      error: (err: AuthError) => {
        // If there is an interaction required error,
        // call one of the interactive methods and then make the request again.
        if (InteractionRequiredAuthError.isInteractionRequiredError(err.errorCode)) {
          this.authService.acquireTokenPopup({
            scopes: this.authService.getScopesForEndpoint(config.resources.todoListApi.resourceUri)
          })
            .then(() => {
              this.service.getTodos()
                .toPromise()
                .then((response: Todo[]) => {
                  this.todos = response;
                });
            });
        }
      }
    });
  }

  addTodo(add: NgForm): void {
    this.service.postTodo(add.value).subscribe(() => {
      console.log('Refreshing List.');
      this.getTodos();
    });
    add.resetForm();
  }

  checkTodo(todo): void {
    this.service.editTodo(todo).subscribe();
  }

  removeTodo(id): void {
    this.service.deleteTodo(id).subscribe(() => {
      this.getTodos();
    });
  }

}
