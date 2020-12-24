import {Injectable} from '@angular/core';
// @ts-ignore
import * as config from './app-config.json';
import {HttpClient} from '@angular/common/http';
import {Todo} from './model/todo';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  url = config.resources.todoListApi.resourceUri;

  constructor(private http: HttpClient) {
  }

  // tslint:disable-next-line:typedef
  getTodos() {
    return this.http.get<Todo[]>(this.url);
  }

  // tslint:disable-next-line:typedef
  getTodo(id) {
    return this.http.get<Todo>(this.url + '/' + id);
  }

  // tslint:disable-next-line:typedef
  postTodo(todo) {
    return this.http.post<Todo>(this.url, todo);
  }

  // tslint:disable-next-line:typedef
  deleteTodo(id) {
    return this.http.delete(this.url + '/' + id);
  }

  // tslint:disable-next-line:typedef
  editTodo(todo) {
    return this.http.put<Todo>(this.url + '/' + todo.id, todo);
  }
}
