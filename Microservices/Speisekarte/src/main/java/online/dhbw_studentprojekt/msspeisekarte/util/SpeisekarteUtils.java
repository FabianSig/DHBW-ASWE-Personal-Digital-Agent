package online.dhbw_studentprojekt.msspeisekarte.util;

import lombok.experimental.UtilityClass;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@UtilityClass
public class SpeisekarteUtils {

    public Speisekarte filterSpeisekarte(Speisekarte speisekarte, List<String> allergene) {

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

    private List<Speisekarte.Speise> filterMeals(List<Speisekarte.Speise> meals, List<String> allergene) {

        return meals.stream()
                .filter(meal -> meal.allergene().stream().noneMatch(allergene::contains))
                .toList();
    }

    public MultiValueMap<String, String> prepareFormData(Optional<String> datumParam) {

        final LocalDate today = handleDate(datumParam);

        final int todayDayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        final String startThisWeek = todayDayOfWeek <= 3 ? LocalDate.now().minusDays(todayDayOfWeek).toString() : LocalDate.now().plusDays(7L - todayDayOfWeek).toString();
        final String startNextWeek = todayDayOfWeek <= 3 ? LocalDate.now().plusDays(7L - todayDayOfWeek).toString() : LocalDate.now().plusDays(14L - todayDayOfWeek).toString();

        // Da es keine vernünftige API gibt, musste ich hier die parameter, welche die website selbst schickt, mitschicken
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(today.toString()));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        return formData;
    }

    private LocalDate handleDate(Optional<String> datumParam) {

        return datumParam
                .filter(not(String::isBlank))
                .map(LocalDate::parse)
                .filter(SpeisekarteUtils::isWeekend)
                .orElse(LocalDate.now());
    }

    private boolean isWeekend(LocalDate date) {

        if (date.getDayOfWeek().getValue() > 5) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Am Wochenende gibt es keine Speisekarte");
        }
        return true;
    }

    public Speisekarte extractMenu(String html) {

        final String[] gruppen = html.split("<div class='col-xs-4 gruppenname'>");
        return new Speisekarte(
                extractGruppe(gruppen[1]),
                extractGruppe(gruppen[2]),
                extractGruppe(gruppen[3]),
                extractGruppe(gruppen[4]),
                extractGruppe(gruppen[5]),
                extractGruppe(gruppen[6]),
                extractGruppe(gruppen[7]));
    }

    private List<Speisekarte.Speise> extractGruppe(String gruppe) {

        final String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .dropWhile(s -> s.contains("<div class='col-xs-8 preise-head'>"))
                .map(SpeisekarteUtils::extractSpeise)
                .toList();
    }

    private Speisekarte.Speise extractSpeise(String s) {

        final String[] split1 = s.split("</span>", 2);
        final String name = split1[0];
        final String[] split2 = split1[1].split("<div class='azn hidden size-13'>", 2)[1].replaceFirst("<br>", "").trim().split("<span style='text-decoration:underline'>Portion</span>", 2);

        final List<String> allergene = extractAllergene(split2[0]);

        final List<Speisekarte.Speise.Naehrwerte> naehrwerte = extractNaehrwerte(split2[1]);

        return new Speisekarte.Speise(name, allergene, naehrwerte);
    }

    private List<String> extractAllergene(String s) {

        if (s.indexOf("<div") < 1) {
            return List.of();
        }

        final String allergene = s
                .substring(1, s.indexOf("<div"))
                .trim();

        final String[] allergeneArr = allergene.substring(0, allergene.length() - 2)
                .split(", ");

        return List.of(allergeneArr);
    }

    private List<Speisekarte.Speise.Naehrwerte> extractNaehrwerte(String s) {

        s = s.substring(s.indexOf("Brennwert"), s.indexOf("</div>"));

        final List<Speisekarte.Speise.Naehrwerte> naehrwerte = new ArrayList<>();

        Arrays.stream(s.split("<br>"))
                .map(String::trim)
                .filter(element -> !element.startsWith("-"))
                .map(element -> element.split(": "))
                .map(element -> new Speisekarte.Speise.Naehrwerte(element[0], element[1]))
                .forEachOrdered(naehrwerte::add);

        return naehrwerte;
    }

}
