package nikomitk.personaldigitalagentmsspeisekarte.dto;

import java.util.List;

public record Speisekarte(List<Speise> vorspeisen, List<Speise> veganerRenner, List<Speise> hauptgericht, List<Speise> beilagen, List<Speise> salat, List<Speise> dessert, List<Speise> buffet) {
    public record Speise(String name, List<String> allergene, List<Naehrwerte> naehrwerte) {
        public record Naehrwerte(String name, String menge){}
    }
}
