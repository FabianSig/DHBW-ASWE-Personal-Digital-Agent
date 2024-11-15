package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.bff.client.PrefsClient;
import online.dhbw_studentprojekt.bff.client.RaplaClient;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import online.dhbw_studentprojekt.dto.trigger.Trigger;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
        String triggerWecker = prefsClient.getPreference("wecker-" + date).map(pref -> pref.value()
                                            .getFirst())
                                            .orElseGet(() -> {
                                                LocalDateTime defaultDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
                                                ZonedDateTime zonedDateTime = defaultDateTime.atZone(ZoneId.of("Europe/Berlin"));
                                                return zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                            });

        triggers.add(new Trigger("/api/logic/morning", triggerWecker));
        triggers.add(new Trigger("/api/logic/mittag", raplaResponse.end_of_first_lecture()));
        triggers.add(new Trigger("/api/logic/abend", raplaResponse.end_of_last_lecture()));

        return new TriggerResponse(triggers);
    }

}
