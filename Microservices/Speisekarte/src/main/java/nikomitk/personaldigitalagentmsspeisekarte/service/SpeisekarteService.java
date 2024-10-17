package nikomitk.personaldigitalagentmsspeisekarte.service;

import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.dto.Speisekarte;
import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SpeisekarteService {


    private final SpeisekarteClient speisekarteClient;

    public SpeisekarteService(SpeisekarteClient speisekarteClient) {
        this.speisekarteClient = speisekarteClient;
    }

    private static List<String> extractMenu(String gruppe) {
        String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }

    public Speisekarte getSpeisekarte(Optional<String> datumParam) {
        String datum = datumParam.orElse(LocalDate.now().toString());

        String startThisWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue()).toString();
        String startNextWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().plusDays(7L - LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(14 - LocalDate.now().getDayOfWeek().getValue()).toString();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(datum));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        String websiteHtml = speisekarteClient.getSpeisekarte(formData);

        String[] gruppen = websiteHtml.split("<div class='col-xs-4 gruppenname'>");


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
