import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule, NgIf, NgFor, DatePipe, CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Project, ProjectService } from '../../services/project.service';
import { Material, MaterialService } from '../../services/material.service';
import { ProjectMaterialCreate, ProjectMaterialRow, ProjectMaterialService } from '../../services/project-material.service';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgIf, NgFor, RouterLink, DatePipe, CurrencyPipe],
  templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit {
  projectId!: number;
  project: Project | null = null;

  materials: Material[] = [];
  /**
   * When using the datalist search, users type a composite string (name and
   * market ID) to select a material.  This helper is used to build that
   * display string.
   */
  formatMaterialOption(m: Material): string {
    return `${m.name} (${m.marketId})`;
  }
  items: ProjectMaterialRow[] = [];

  form!: FormGroup;
  loading = false;

  /**
   * Form for editing project metadata such as estimated end date and
   * finished status.  Bound to controls in the template.  Values are
   * initialised once the project data has been loaded.
   */
  editForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private projectsApi: ProjectService,
    private materialsApi: MaterialService,
    private pmApi: ProjectMaterialService
  ) {}

  ngOnInit(): void {
    this.projectId = +(this.route.snapshot.paramMap.get('id') || 0);

    this.form = this.fb.group({
      materialId: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0)]]
    });

    // Load project from list (no single-get endpoint on backend)
    this.projectsApi.list().subscribe(list => {
      this.project = list.find(p => p.id === this.projectId) || null;
      // Initialise edit form once project data has been retrieved
      this.initEditForm();
    });

    this.materialsApi.list().subscribe(ms => (this.materials = ms));
    this.reload();
  }

  /**
   * Initialise the edit form with the current project's metadata.  Converts
   * existing ISO timestamp strings to values compatible with the date picker.
   */
  initEditForm() {
    // Extract only the date portion (YYYY-MM-DD) for the date picker.
    const est = this.project?.estimatedEnd ? this.project.estimatedEnd.slice(0, 10) : '';
    const finished = !!this.project?.finishedAt;
    this.editForm = this.fb.group({
      estimatedEnd: [est],
      finished: [finished]
    });
  }

  /**
   * Called when the user selects or types a value in the datalist input.
   * Attempts to find a matching material based on the composite string
   * displayed in the list.  If a match is found, the materialId form control
   * is updated with the selected material’s id; otherwise it is cleared.
   */
  onMaterialSelectChange(selection: string) {
    const match = this.materials.find(m => this.formatMaterialOption(m) === selection);
    const id = match ? match.id : '';
    this.form.get('materialId')?.setValue(id);
  }

  /**
   * Persist changes to the project's estimated end and finished status.  If
   * finished is checked the current time is used for the finishedAt
   * timestamp; otherwise it is cleared.  The estimated end date is
   * converted to a YYYY-MM-DDT00:00:00 string so no timezone shifting occurs.
   */
  saveProject() {
    if (!this.project) return;
    const estVal: string = this.editForm.value.estimatedEnd;
    let estimatedEnd: string | null = null;
    if (estVal) {
      // For a date input, append midnight time in ISO format without timezone.
      estimatedEnd = `${estVal}T00:00:00`;
    }
    const finished: boolean = this.editForm.value.finished;
    let finishedAt: string | null = null;
    if (finished) {
      const now = new Date();
      // Build a local ISO string (without timezone) to avoid timezone offsets
      const pad = (n: number) => n.toString().padStart(2, '0');
      finishedAt = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}` +
                   `T${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
    }
    const payload = {
      name: this.project.name,
      description: this.project.description,
      estimatedEnd: estimatedEnd,
      finishedAt: finishedAt
    };
    this.loading = true;
    this.projectsApi.update(this.projectId, payload).subscribe({
      next: updated => {
        // update local project and reinitialise edit form
        this.project = updated as any;
        this.initEditForm();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  reload() {
    this.pmApi.list(this.projectId).subscribe(rows => (this.items = rows));
  }

  add() {
    if (this.form.invalid) return;
    const payload: ProjectMaterialCreate = {
      materialId: +this.form.value.materialId,
      amount: +this.form.value.amount
    };
    this.loading = true;
    this.pmApi.create(this.projectId, payload).subscribe({
      next: created => {
        // If upsert, replace existing; otherwise push
        const idx = this.items.findIndex(x => x.materialId === created.materialId);
        if (idx >= 0) this.items[idx] = created; else this.items = [...this.items, created];
        this.form.reset({ materialId: '', amount: 0 });
        this.loading = false;
      },
      error: () => (this.loading = false)
    });
  }

  remove(id: number) {
    this.pmApi.delete(this.projectId, id).subscribe(() => {
      this.items = this.items.filter(x => x.id !== id);
    });
  }

  back() {
    this.router.navigate(['/projects']);
  }
}
