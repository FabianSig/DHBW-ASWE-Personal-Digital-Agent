package online.dhbw_studentprojekt.msspeisekarte.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.msspeisekarte.client.SpeisekarteClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static online.dhbw_studentprojekt.msspeisekarte.util.SpeisekarteUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpeisekarteService {

    private final SpeisekarteClient speisekarteClient;

    private static MultiValueMap<String, String> prepareFormData(String datum) {

        final int todayDayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        final String startThisWeek = todayDayOfWeek <= 3 ? LocalDate.now().minusDays(todayDayOfWeek).toString() : LocalDate.now().plusDays(7L - todayDayOfWeek).toString();
        final String startNextWeek = todayDayOfWeek <= 3 ? LocalDate.now().plusDays(7L - todayDayOfWeek).toString() : LocalDate.now().plusDays(14L - todayDayOfWeek).toString();

        // Da es keine vernünftige API gibt, musste ich hier die parameter, welche die website selbst schickt, mitschicken
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(datum));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        return formData;
    }

    private static List<Speisekarte.Speise> filterMeals(List<Speisekarte.Speise> meals, List<String> allergene) {

        return meals.stream()
                .filter(meal -> meal.allergene().stream().noneMatch(allergene::contains))
                .collect(Collectors.toList());
    }

    public Speisekarte getSpeisekarte(Optional<String> datumParam) {

        final String datum = datumParam.filter(not(String::isBlank)).orElse(LocalDate.now().toString());

        if (LocalDate.parse(datum).getDayOfWeek().getValue() > 5) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Am Wochenende gibt es keine Speisekarte");
        }

        final String websiteHtml = speisekarteClient.getSpeisekarte(prepareFormData(datum));

        return extractMenu(websiteHtml);
    }

    public Speisekarte getSpeisekarteWithFilteredAllergene(Optional<String> date, List<String> allergene) {

        Speisekarte speisekarte = this.getSpeisekarte(date);

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

}
