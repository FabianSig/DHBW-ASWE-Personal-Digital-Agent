package fabiansig.service;

import fabiansig.client.SpeisekarteClient;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;

public class SpeisekarteService {

    private SpeisekarteClient speisekarteClient;

    public Speisekarte getSpeisekarte(String date) {
        return speisekarteClient.getSpeisekarte(date);
    }
}
