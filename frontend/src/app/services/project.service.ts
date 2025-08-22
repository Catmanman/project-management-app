import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Project {
  id: number;
  userId: number;
  name: string;
  description: string;
  createdAt?: string | null;
  estimatedEnd?: string | null;
  finishedAt?: string | null;
}

/**
 * Client service for project CRUD operations.  All calls return
 * observables from HttpClient.  The JWT interceptor appends
 * credentials automatically.
 */
@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly base = 'http://localhost:8080/api/projects';
  constructor(private http: HttpClient) {}
  list(): Observable<Project[]> {
    return this.http.get<Project[]>(this.base);
  }
  create(data: Omit<Project, 'id' | 'userId' | 'createdAt'> & { userId?: number }): Observable<Project> {
    // The backend ignores userId and derives from token; we include a
    // placeholder value for compatibility with the DTO but it is
    // ultimately unused.  Passing undefined will omit the property.
    return this.http.post<Project>(this.base, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  /**
   * Update an existing project.  Accepts the project id and a partial
   * payload containing any fields to update (name, description,
   * estimatedEnd, finishedAt).  Returns the updated project from the
   * backend.
   */
  update(id: number, data: Partial<Project>): Observable<Project> {
    return this.http.put<Project>(`${this.base}/${id}`, data);
  }
}