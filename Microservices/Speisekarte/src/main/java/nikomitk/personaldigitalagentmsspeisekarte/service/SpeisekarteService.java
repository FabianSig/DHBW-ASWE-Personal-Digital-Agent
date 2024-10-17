package nikomitk.personaldigitalagentmsspeisekarte.service;

import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SpeisekarteService {


    private final SpeisekarteClient speisekarteClient;

    public SpeisekarteService(SpeisekarteClient speisekarteClient) {
        this.speisekarteClient = speisekarteClient;
    }

    public List<String> getSpeisekarte() {
        String today = LocalDate.now().toString();
        String startThisWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue()).toString();
        String startNextWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().plusDays(7L - LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(14 - LocalDate.now().getDayOfWeek().getValue()).toString();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of(today));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of(startThisWeek));
        formData.put("startNextWeek", List.of(startNextWeek));

        String websiteHtml = speisekarteClient.getSpeisekarte(formData);

        String[] split = websiteHtml.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }
}
