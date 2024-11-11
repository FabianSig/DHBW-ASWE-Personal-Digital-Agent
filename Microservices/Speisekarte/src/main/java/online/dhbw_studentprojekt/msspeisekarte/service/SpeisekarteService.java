package online.dhbw_studentprojekt.msspeisekarte.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.msspeisekarte.client.SpeisekarteClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static online.dhbw_studentprojekt.msspeisekarte.util.SpeisekarteUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpeisekarteService {

    private final SpeisekarteClient speisekarteClient;

    public Speisekarte getSpeisekarteWithFilteredAllergene(Optional<String> date, List<String> allergene) {

        Speisekarte speisekarte = this.getSpeisekarte(date);
        if (allergene == null || allergene.isEmpty()) {
            return speisekarte;
        }

        return filterSpeisekarte(speisekarte, allergene);
    }


    public Speisekarte getSpeisekarte(Optional<String> datumParam) {

        final String websiteHtml = speisekarteClient.getSpeisekarte(prepareFormData(datumParam));

        return extractMenu(websiteHtml);
    }

}
