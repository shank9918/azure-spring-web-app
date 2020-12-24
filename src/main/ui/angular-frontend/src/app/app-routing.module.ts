import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {TodoEditComponent} from './todo-edit/todo-edit.component';
import {TodoViewComponent} from './todo-view/todo-view.component';
import {MsalGuard} from '@azure/msal-angular';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'todo-edit/:id',
    component: TodoEditComponent,
    canActivate: [
      MsalGuard
    ]
  },
  {
    path: 'todo-view',
    component: TodoViewComponent,
    canActivate: [
      MsalGuard
    ]
  }
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes, {useHash: false})
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
