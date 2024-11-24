package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.PrefsClient;
import online.dhbw_studentprojekt.bff.client.RaplaClient;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import online.dhbw_studentprojekt.dto.trigger.Trigger;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
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
                                                ZoneId zoneId = ZoneId.of("Europe/Berlin");
                                                LocalDateTime localDateTime = LocalDateTime.now(zoneId).withHour(8).withMinute(0).withSecond(0).withNano(0);
                                                ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
                                                return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                                            });

        triggers.add(new Trigger("/api/logic/morning", triggerWecker));
        triggers.add(new Trigger("/api/logic/mittag", raplaResponse.end_of_first_lecture()));
        triggers.add(new Trigger("/api/logic/abend", raplaResponse.end_of_last_lecture()));

        return new TriggerResponse(triggers);
    }

    public TriggerResponse getMockTrigger(String date) {


        List<Trigger> triggers = new ArrayList<>();
        log.debug("Getting Trigger for: wecker-{}", date);
        String triggerWecker = prefsClient.getPreference("wecker-" + date).map(pref -> pref.value()
                        .getFirst())
                .orElseGet(() -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                });

        log.debug("Retrieved wecker: {}", triggerWecker);


        LocalDateTime weckerDateTime;

        try {
            weckerDateTime = LocalDateTime.parse(triggerWecker, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        } catch (DateTimeParseException e) {
            log.error("Invalid triggerWecker format: {}", triggerWecker, e);
            weckerDateTime = LocalDateTime.now().plusMinutes(1);
        }

        triggers.add(new Trigger("/api/logic/morning", weckerDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        triggers.add(new Trigger("/api/logic/mittag", weckerDateTime.plusSeconds(20).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        triggers.add(new Trigger("/api/logic/abend", weckerDateTime.plusSeconds(20).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        return new TriggerResponse(triggers);
    }

}
