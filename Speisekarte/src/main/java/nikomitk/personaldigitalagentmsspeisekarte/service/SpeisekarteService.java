package nikomitk.personaldigitalagentmsspeisekarte.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import nikomitk.personaldigitalagentmsspeisekarte.dto.Speisekarte;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpeisekarteService {


    private final SpeisekarteClient speisekarteClient;

    private static List<String> extractMenu(String gruppe) {
        final String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }

    public Speisekarte getSpeisekarte(Optional<String> datumParam) {
        final String datum = datumParam.filter(Predicate.not(String::isBlank)).orElse(LocalDate.now().toString());

        if (LocalDate.parse(datum).getDayOfWeek().getValue() > 5) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Am Wochenende gibt es keine Speisekarte");
        }

        final int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        final String startThisWeek = dayOfWeek <= 3 ? LocalDate.now().minusDays(dayOfWeek).toString() : LocalDate.now().plusDays(7 - dayOfWeek).toString();
        final String startNextWeek = dayOfWeek <= 3 ? LocalDate.now().plusDays(7L - dayOfWeek).toString() : LocalDate.now().plusDays(14 - dayOfWeek).toString();

        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(datum));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        final String websiteHtml = speisekarteClient.getSpeisekarte(formData);

        final String[] gruppen = websiteHtml.split("<div class='col-xs-4 gruppenname'>");


        return new Speisekarte(
                extractMenu(gruppen[1]),
                extractMenu(gruppen[2]),
                extractMenu(gruppen[3]),
                extractMenu(gruppen[4]),
                extractMenu(gruppen[5]),
                extractMenu(gruppen[6]),
                extractMenu(gruppen[7]));
    }
}
