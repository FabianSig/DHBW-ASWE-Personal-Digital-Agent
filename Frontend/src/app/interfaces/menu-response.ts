export interface MenuResponse {
  vorspeisen: Speise[];
  veganerRenner: Speise[];
  hauptgericht: Speise[];
  beilagen: Speise[];
  salat: Speise[];
  dessert: Speise[];
  buffet: Speise[];
}

export interface Speise {
  name: string;
  allergene: string[];
  naehrwerte: Naehrwert[];
}

export interface Naehrwert {
  name: string;
  menge: string;
}
