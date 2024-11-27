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

    /**
     * Retrieves a Speisekarte and filters out meals containing specified allergens.
     *
     * @param date      an optional date parameter to fetch the Speisekarte for a specific date
     * @param allergene a list of allergens to filter out from the Speisekarte
     * @return a Speisekarte with meals filtered based on the specified allergens
     */
    public Speisekarte getSpeisekarteWithFilteredAllergene(Optional<String> date, List<String> allergene) {

        Speisekarte speisekarte = this.getSpeisekarte(date);

        // If no allergene are specified, return the unfiltered speisekarte
        if (allergene == null || allergene.isEmpty()) {
            return speisekarte;
        }

        return filterSpeisekarte(speisekarte, allergene);
    }

    /**
     * Retrieves a Speisekarte for a specific date.
     *
     * @param datumParam an optional date parameter to fetch the Speisekarte for a specific date
     * @return a Speisekarte for the specified date
     */
    public Speisekarte getSpeisekarte(Optional<String> datumParam) {

        final String websiteHtml = speisekarteClient.getSpeisekarte(prepareFormData(datumParam));

        return extractMenu(websiteHtml);
    }

}
