// ...existing imports...
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);

   
  private base = 'http://localhost:8080/users';
   

 
  deleteMe() { return this.http.delete<void>(`${this.base}/me`); }
  deleteById(id: number) { return this.http.delete<void>(`${this.base}/${id}`); }
  deleteMeAsync() { return firstValueFrom(this.deleteMe()); }
  deleteByIdAsync(id: number) { return firstValueFrom(this.deleteById(id)); }

  
  updateMyUsername(currentPassword: string, newUsername: string) {
    return this.http.put<void>(`${this.base}/me/username`, { currentPassword, newUsername });
  }
  updateMyUsernameAsync(currentPassword: string, newUsername: string) {
    return firstValueFrom(this.updateMyUsername(currentPassword, newUsername));
  }

   
  updateMyPassword(currentPassword: string, newPassword: string) {
    return this.http.put<void>(`${this.base}/me/password`, { currentPassword, newPassword });
  }
  updateMyPasswordAsync(currentPassword: string, newPassword: string) {
    return firstValueFrom(this.updateMyPassword(currentPassword, newPassword));
  }
}
