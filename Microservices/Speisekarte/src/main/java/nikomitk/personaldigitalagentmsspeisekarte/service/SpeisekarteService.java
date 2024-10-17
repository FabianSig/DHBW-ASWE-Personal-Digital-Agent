package nikomitk.personaldigitalagentmsspeisekarte.service;

import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpeisekarteService {


    private final SpeisekarteClient speisekarteClient;

    public SpeisekarteService(SpeisekarteClient speisekarteClient) {
        this.speisekarteClient = speisekarteClient;
    }

    public List<String> getSpeisekarte() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("func", List.of("make_spl"));
        formData.put("locId", List.of("16"));
        formData.put("date", List.of("2024-10-17"));
        formData.put("lang", List.of("de"));
        formData.put("startThisWeek", List.of("2024-10-14"));
        formData.put("startNextWeek", List.of("2024-10-21"));

        String websiteHtml = speisekarteClient.getSpeisekarte(formData);

        String [] split = websiteHtml.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }
}
