import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MaterialService, Material } from '../../services/material.service';
import { transliterate } from 'app/utils/transliterate';

/**
 * Component for displaying and managing the list of materials.
 * Admin users can add and delete materials; regular users can only view them.
 * Attempting to modify materials as a non-admin will result in a 403 error which is silently ignored here.
 */
@Component({
  selector: 'app-materials-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './materials-list.component.html'
})
export class MaterialsListComponent implements OnInit {
  items: Material[] = [];

  searchTerm = '';
  latinTerm = '';
  form!: FormGroup;
  loading = false;

  // ---- Image helpers ----
  private readonly ASSETS_BASE = 'assets/images/default_material_png';
  // conservative default that exists in your folder
  private readonly defaultImage = `${this.ASSETS_BASE}/hammer.png`;
  // map common keywords to filenames in /assets/images/default_material_png
  private readonly keywordToFile: Record<string, string> = {
    laminat: 'laminat.png',
    laminate: 'laminat.png',
    hose: 'hose.png',
    lock: 'lock.png',
    libela: 'libela.png',
    level: 'libela.png',
    drill: 'drillbit.png',
    drillbit: 'drillbit.png',
    gloves: 'gloves.png',
    hammer: 'hammer.png',
    pants: 'pants.png',
    pliers: 'pliers.png',
    pvc: 'pvc_pipes.png',
    pipe: 'pvc_pipes.png',
    screw: 'screw.png',
    screwdriver: 'screwdriver.png',
    shirt: 'shirt.png',
    shoes: 'shoes.png',
    wrench: 'wrench.png'
  };

  constructor(private fb: FormBuilder, private api: MaterialService) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      marketId: ['', Validators.required],
      seller: ['', Validators.required],
      // keep the control name aligned with backend JSON ("pictureUrl")
      pictureUrl: ['']
    });
    this.load();
  }

  load() {
    this.api.list().subscribe(items => (this.items = items));
  }

  add() {
    if (this.form.invalid) return;

    const { name, marketId, seller, pictureUrl } = this.form.value;
    this.loading = true;

    this.api.create({ name, marketId, seller, pictureUrl }).subscribe({
      next: created => {
        this.items = [...this.items, created];
        this.form.reset({ name: '', marketId: '', seller: '', pictureUrl: '' });
        this.loading = false;
      },
      error: () => {
        // ignore errors (likely 403)
        this.loading = false;
      }
    });
  }

  remove(id: number) {
    this.api.delete(id).subscribe(() => {
      this.items = this.items.filter(m => m.id !== id);
    });
  }

  onSearchChange(term: string) {
    this.latinTerm = (term ?? '').toLowerCase();
    this.searchTerm = transliterate(term ?? '').toLowerCase();
  }

  get filteredItems(): Material[] {
    const cyrTerm = this.searchTerm;
    const latTerm = this.latinTerm;
    if (!cyrTerm && !latTerm) return this.items;
    return this.items.filter(item => {
      const name = (item.name ?? '').toLowerCase();
      const market = (item.marketId ?? '').toLowerCase();
      const matchesName =
        (cyrTerm && name.includes(cyrTerm)) || (latTerm && name.includes(latTerm));
      const matchesMarket = !!latTerm && market.includes(latTerm);
      return matchesName || matchesMarket;
    });
  }

  /** Returns a safe image URL for the material, with fallbacks. */
  imageSrc(m: Material): string {
    let raw = (m.pictureUrl ?? '').trim();

    if (!raw) {
      // guess from name/marketId keywords if no explicit URL
      const hay = `${m.name ?? ''} ${m.marketId ?? ''}`.toLowerCase();
      for (const key of Object.keys(this.keywordToFile)) {
        if (hay.includes(key)) {
          return `${this.ASSETS_BASE}/${this.keywordToFile[key]}`;
        }
      }
      return this.defaultImage;
    }

    // Normalize backslashes and a common DB typo ("deffault" -> "default")
    raw = raw.replace(/\\/g, '/').replace('deffault_material_png', 'default_material_png');

    // absolute URL provided by backend/user
    if (/^https?:\/\//i.test(raw)) return raw;

    // in-app path like "assets/…"
    if (raw.startsWith('assets/')) return raw;

    // If DB only stores a filename, map it into your default images folder
    // (supports png/jpg/jpeg/webp filenames)
    return `${this.ASSETS_BASE}/${raw}`;
  }

  /**
   * Returns seller logo URL if we can infer one, else null.
   * - If seller is already an asset path (starts with assets/), return it.
   * - Otherwise, slugify seller and point to assets/images/producers/<slug>.png
   *   (example: "ekskluziv" -> assets/images/producers/ekskluziv.png).
   */
  sellerLogo(seller: string | null | undefined): string | null {
    if (!seller) return null;

    const trimmed = seller.trim();

    // If someone stored a direct path already
    if (trimmed.startsWith('assets/')) {
      return trimmed.replace(/\\/g, '/');
    }

    // Slugify: lowercase, trim, replace spaces/underscores with '-', remove non-word chars
    const slug = trimmed
      .toLowerCase()
      .replace(/[_\s]+/g, '-')
      .replace(/[^a-z0-9-]/g, '');

    if (!slug) return null;

    return `assets/images/producers/${slug}.png`;
  }

  /** Optional for *ngFor performance */
  trackById(_: number, m: Material) {
    return m.id;
  }
}
