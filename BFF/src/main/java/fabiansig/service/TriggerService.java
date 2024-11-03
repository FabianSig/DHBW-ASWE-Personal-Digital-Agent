package fabiansig.service;

import fabiansig.client.RaplaClient;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TriggerService {

    private final RaplaClient raplaClient;

    public List<String> getTrigger(String date) {

        RaplaResponse raplaResponse = raplaClient.getLectureTimes(date);

        return List.of(raplaResponse.start_of_first_lecture(), raplaResponse.end_of_first_lecture(), raplaResponse.end_of_last_lecture());
    }

    public List<String> getTrigger() {
        return getTrigger(LocalDate.now().toString());
    }
}
