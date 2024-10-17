package nikomitk.personaldigitalagentmsspeisekarte.dto;

import java.util.List;

public record Speisekarte(List<String> vorspeisen, List<String> veganerRenner, List<String> hauptgericht, List<String> beilagen, List<String> salat, List<String> dessert, List<String> buffet) {
}
