import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService, Project } from '../../services/project.service';
import { NgIf, NgFor, DatePipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgIf, NgFor, DatePipe],
  templateUrl: './projects-list.component.html'
})
export class ProjectsListComponent implements OnInit {
  items: Project[] = [];
  form!: FormGroup;
  loading = false;

  constructor(private fb: FormBuilder, private api: ProjectService, private router: Router) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      estimatedEnd: [''],
      finishedAt: ['']
    });
    this.load();
  }

  load() {
    this.api.list().subscribe(items => (this.items = items));
  }

  add() {
    if (this.form.invalid) return;
    const raw = this.form.getRawValue();
    const dto = {
      name: raw.name!,
      description: raw.description!,
      estimatedEnd: raw.estimatedEnd || null,
      finishedAt: raw.finishedAt || null
    } as any;

    this.loading = true;
    this.api.create(dto).subscribe({
      next: created => {
        this.items = [...this.items, created];
        this.form.reset({ name: '', description: '', estimatedEnd: '', finishedAt: '' });
        this.loading = false;
      },
      error: () => (this.loading = false)
    });
  }

  remove(id: number) {
    this.api.delete(id).subscribe(() => {
      this.items = this.items.filter(p => p.id !== id);
    });
  }

  view(id: number) {
    this.router.navigate(['/projects', id]);
  }
}
