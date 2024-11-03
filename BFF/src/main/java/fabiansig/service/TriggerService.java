package fabiansig.service;

import fabiansig.client.PrefsClient;
import fabiansig.client.RaplaClient;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import online.dhbw_studentprojekt.dto.trigger.Trigger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TriggerService {

    private final RaplaClient raplaClient;
    private final PrefsClient prefsClient;

    public TriggerResponse getTrigger(String date) {

        List<Trigger> triggers = new ArrayList<>();

        RaplaResponse raplaResponse = raplaClient.getLectureTimes(date);
        String triggerWecker = prefsClient.getPreference("wecker-" + date).value().getFirst();

        triggers.add(new Trigger("/api/logic/morning", triggerWecker));
        triggers.add(new Trigger("/api/logic/mittag", raplaResponse.end_of_first_lecture()));
        triggers.add(new Trigger("/api/logic/abend", raplaResponse.end_of_last_lecture()));

        return new TriggerResponse(triggers);
    }

}
