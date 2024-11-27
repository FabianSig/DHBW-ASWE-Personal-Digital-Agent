package online.dhbw_studentprojekt.bff.service;

import online.dhbw_studentprojekt.bff.client.PrefsClient;
import online.dhbw_studentprojekt.bff.client.RaplaClient;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BFFTriggerServiceTest {

    private final RaplaClient raplaClient = Mockito.mock(RaplaClient.class);
    private final PrefsClient prefsClient = Mockito.mock(PrefsClient.class);
    private final TriggerService triggerService = new TriggerService(raplaClient, prefsClient);

    @Test
    void testGetTriggerWithPreferences() {
        // Given
        String date = "2024-11-20";
        String mockPreference = "2024-11-27T08:00:00+01:00";
        RaplaResponse mockRaplaResponse = new RaplaResponse("2024-11-20T09:00:00+01:00","2024-11-20T12:00:00+01:00" ,"2024-11-20T18:00:00+01:00");

        when(prefsClient.getPreference("wecker-" + date))
                .thenReturn(Optional.of(new Preference("wecker-" + date, List.of(mockPreference))));
        when(raplaClient.getLectureTimes(date)).thenReturn(mockRaplaResponse);

        // When
        TriggerResponse response = triggerService.getTrigger(date);
        // Then
        assertEquals(3, response.triggers().size());
        assertEquals("/api/logic/morning", response.triggers().get(0).route());
        assertEquals(mockPreference, response.triggers().get(0).time());
        assertEquals("/api/logic/mittag", response.triggers().get(1).route());
        assertEquals("2024-11-20T12:00:00+01:00", response.triggers().get(1).time());
        assertEquals("/api/logic/abend", response.triggers().get(2).route());
        assertEquals("2024-11-20T18:00:00+01:00", response.triggers().get(2).time());
    }

    @Test
    void testGetTriggerWithoutPreferences() {
        // Given
        String date = "2024-11-20";
        RaplaResponse mockRaplaResponse = new RaplaResponse("2024-11-20T09:00:00+01:00", "2024-11-20T12:00:00+01:00", "2024-11-20T18:00:00+01:00");
        when(prefsClient.getPreference("wecker-" + date)).thenReturn(Optional.empty());
        when(raplaClient.getLectureTimes(date)).thenReturn(mockRaplaResponse);

        // Calculate expected default time
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        ZonedDateTime defaultTime = ZonedDateTime.now(zoneId).withHour(8).withMinute(0).withSecond(0).withNano(0);
        String expectedDefaultTime = defaultTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // When
        TriggerResponse response = triggerService.getTrigger(date);

        // Then
        assertEquals(3, response.triggers().size());
        assertEquals("/api/logic/morning", response.triggers().get(0).route());
        assertEquals(expectedDefaultTime, response.triggers().get(0).time());
        assertEquals("/api/logic/mittag", response.triggers().get(1).route());
        assertEquals("2024-11-20T12:00:00+01:00", response.triggers().get(1).time());
        assertEquals("/api/logic/abend", response.triggers().get(2).route());
        assertEquals("2024-11-20T18:00:00+01:00", response.triggers().get(2).time());
    }
}

