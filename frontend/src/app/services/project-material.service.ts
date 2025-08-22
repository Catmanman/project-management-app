import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProjectMaterialRow {
  id: number;
  materialId: number;
  materialName: string;
  marketId: string;
  amount: number;
}

export interface ProjectMaterialCreate {
  materialId: number;
  amount: number;
}

@Injectable({ providedIn: 'root' })
export class ProjectMaterialService {
  private readonly base = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  list(projectId: number): Observable<ProjectMaterialRow[]> {
    return this.http.get<ProjectMaterialRow[]>(`${this.base}/${projectId}/materials`);
    }
  create(projectId: number, data: ProjectMaterialCreate): Observable<ProjectMaterialRow> {
    return this.http.post<ProjectMaterialRow>(`${this.base}/${projectId}/materials`, data);
  }
  delete(projectId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${projectId}/materials/${id}`);
  }
}
