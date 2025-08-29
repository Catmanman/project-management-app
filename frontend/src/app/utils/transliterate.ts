// Standalone, reusable function (no class needed)
export function transliterate(input: string): string {
  // mapping for digraphs (two-letter combinations)
  const digraphs: Record<string, string> = {
    gj: 'ѓ', zh: 'ж', dz: 'ѕ', lj: 'љ', nj: 'њ', kj: 'ќ', ch: 'ч', sh: 'ш',
  };

  const map: Record<string, string> = {
    a: 'а', b: 'б', c: 'ц', d: 'д', e: 'е', f: 'ф',
    g: 'г', h: 'х', i: 'и', j: 'ј', k: 'к', l: 'л',
    m: 'м', n: 'н', o: 'о', p: 'п', r: 'р', s: 'с',
    t: 'т', u: 'у', v: 'в', z: 'з', x: 'кс', y: 'ј', w: 'в',
    q: 'к',
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
