import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login.component';
import { RegisterComponent } from './features/auth/register.component';
import { ProjectsListComponent } from './features/projects/projects-list.component';
import { ProjectDetailComponent } from './features/projects/project-detail.component';
import { MaterialsListComponent } from './features/materials/materials-list.component';
import { AccountDeleteComponent } from './features/auth/account-delete/account-delete.component';
import { authGuard } from './services/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/projects', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'account/delete', component: AccountDeleteComponent },
  // project detail BEFORE list to avoid matching conflicts
  { path: 'projects/:id', component: ProjectDetailComponent, canActivate: [authGuard] },
  { path: 'projects', component: ProjectsListComponent, canActivate: [authGuard] },

  { path: 'materials', component: MaterialsListComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/projects' }
];
