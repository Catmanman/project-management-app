import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Material {
  id: number;
  name: string;
  marketId: string;
}

@Injectable({ providedIn: 'root' })
export class MaterialService {
  private readonly base = 'http://localhost:8080/api/materials';
  constructor(private http: HttpClient) {}
  list(): Observable<Material[]> {
    return this.http.get<Material[]>(this.base);
  }
  create(data: Omit<Material, 'id'>): Observable<Material> {
    return this.http.post<Material>(this.base, data);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}