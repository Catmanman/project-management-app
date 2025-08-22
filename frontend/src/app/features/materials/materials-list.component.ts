import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MaterialService, Material } from '../../services/material.service';

/**
 * Component for displaying and managing the list of materials.  Admin
 * users can add and delete materials; regular users can only view
 * them.  Attempting to modify materials as a non-admin will result
 * in a 403 error which is silently ignored here.
 */
@Component({
  selector: 'app-materials-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgFor, NgIf],
  templateUrl: './materials-list.component.html'
})
export class MaterialsListComponent implements OnInit {
  items: Material[] = [];
  /**
   * Holds the search text used to filter the list of materials.  This value
   * updates via the (input) handler in the template and is used by the
   * filteredItems getter to narrow down the displayed rows.
   */
  searchTerm = '';
  /**
   * Holds the raw search input in Latin script.  This is retained so that
   * filtering can match against both the transliterated (Cyrillic) and
   * original (Latin) terms.  It is always stored in lowercase.
   */
  latinTerm = '';
  form!: FormGroup;
  loading = false;

  constructor(private fb: FormBuilder, private api: MaterialService) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      marketId: ['', Validators.required]
    });
    this.load();
  }

  load() {
    this.api.list().subscribe(items => (this.items = items));
  }

  add() {
    if (this.form.invalid) return;
    const { name, marketId } = this.form.value;
    this.loading = true;
    this.api.create({ name, marketId }).subscribe({
      next: created => {
        this.items = [...this.items, created];
        this.form.reset({ name: '', marketId: '' });
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

  /**
   * Handler for search box input changes.  Converts the incoming term to
   * lowercase for case‑insensitive comparison.  Additionally transliterates
   * from Latin script to Cyrillic (Macedonian phonetic approximation) so
   * that searching with Latin characters (e.g. "Cetka") will match Cyrillic
   * names such as "Четка".
   */
  onSearchChange(term: string) {
    // store the raw latin term for market ID matching
    this.latinTerm = term.toLowerCase();
    // transliterate latin to cyrillic to improve matching on Cyrillic names
    this.searchTerm = this.transliterate(term).toLowerCase();
  }

  /**
   * Computed property returning the list of materials filtered by the current
   * search term.  Returns all items when both search terms are empty.  Filtering
   * matches against both the name and the market ID using the transliterated
   * and original search strings.
   */
  get filteredItems(): Material[] {
    const cyrTerm = this.searchTerm;
    const latTerm = this.latinTerm;
    if (!cyrTerm && !latTerm) return this.items;
    return this.items.filter(item => {
      const name = item.name.toLowerCase();
      const market = item.marketId.toLowerCase();
      // match against both transliterated Cyrillic and original latin
      const matchesName =
        (cyrTerm && name.includes(cyrTerm)) || (latTerm && name.includes(latTerm));
      const matchesMarket = latTerm && market.includes(latTerm);
      return matchesName || matchesMarket;
    });
  }

  /**
   * Transliterate a string from Latin to Macedonian Cyrillic using a
   * simplified phonetic mapping.  Handles common digraphs first (e.g. "ch")
   * and falls back to single character replacements.  Characters not found
   * in the mapping are returned unchanged.
   */
  private transliterate(input: string): string {
    // mapping for digraphs (two-letter combinations)
    const digraphs: { [key: string]: string } = {
      'gj': 'ѓ', 'zh': 'ж', 'dz': 'ѕ', 'lj': 'љ', 'nj': 'њ', 'kj': 'ќ', 'ch': 'ч', 'sh': 'ш'
    };
    const map: { [key: string]: string } = {
      'a': 'а', 'b': 'б', 'c': 'ц', 'd': 'д', 'e': 'е', 'f': 'ф',
      'g': 'г', 'h': 'х', 'i': 'и', 'j': 'ј', 'k': 'к', 'l': 'л',
      'm': 'м', 'n': 'н', 'o': 'о', 'p': 'п', 'r': 'р', 's': 'с',
      't': 'т', 'u': 'у', 'v': 'в', 'z': 'з', 'x': 'кс', 'y': 'ј', 'w': 'в',
      'q': 'к'
    };
    let result = '';
    let i = 0;
    const lower = input.toLowerCase();
    while (i < lower.length) {
      // check digraph
      const dg = lower.substring(i, i + 2);
      if (digraphs[dg]) {
        result += digraphs[dg];
        i += 2;
        continue;
      }
      const ch = lower[i];
      result += map[ch] ?? ch;
      i += 1;
    }
    return result;
  }
}
