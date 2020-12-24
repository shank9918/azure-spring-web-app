import { Component, OnInit } from '@angular/core';
import {Todo} from '../model/todo';
import {ActivatedRoute, Router} from '@angular/router';
import {TodoService} from '../todo.service';

@Component({
  selector: 'app-todo-edit',
  templateUrl: './todo-edit.component.html',
  styleUrls: ['./todo-edit.component.css']
})
export class TodoEditComponent implements OnInit {

  todo: Todo = {
    id: undefined,
    todoItem: undefined,
    status: undefined,
  };

  private route: ActivatedRoute;

  constructor(route: ActivatedRoute, private router: Router, private service: TodoService) {
    this.route = route;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      console.log(id);
      this.service.getTodo(id).subscribe((response: Todo) => {
        this.todo = response;
      });
    });
  }

  editTodo(todo): void {
    this.todo.todoItem = todo.todoItem;
    this.service.editTodo(this.todo).subscribe(() => {
      this.router.navigate(['/todo-view']);
    });
  }

}
