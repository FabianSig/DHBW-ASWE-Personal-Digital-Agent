package fabiansig.service;

import fabiansig.client.SpeisekarteClient;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeisekarteService {

    private final SpeisekarteClient speisekarteClient;

    public Speisekarte getSpeisekarte(String date) {
        return speisekarteClient.getSpeisekarte(date);
    }


    public Speisekarte getSpeisekarteWithFilteredAllergene(String date, List<String> allergene) {

        Speisekarte speisekarte = speisekarteClient.getSpeisekarte(date);

        return new Speisekarte(
                filterMeals(speisekarte.vorspeisen(), allergene),
                filterMeals(speisekarte.veganerRenner(), allergene),
                filterMeals(speisekarte.hauptgericht(), allergene),
                filterMeals(speisekarte.beilagen(), allergene),
                filterMeals(speisekarte.salat(), allergene),
                filterMeals(speisekarte.dessert(), allergene),
                filterMeals(speisekarte.buffet(), allergene)
        );
    }

    private static List<Speisekarte.Speise> filterMeals(List<Speisekarte.Speise> meals, List<String> allergene) {
        return meals.stream()
                .filter(meal -> meal.allergene().stream().noneMatch(allergene::contains))
                .collect(Collectors.toList());
    }


}
