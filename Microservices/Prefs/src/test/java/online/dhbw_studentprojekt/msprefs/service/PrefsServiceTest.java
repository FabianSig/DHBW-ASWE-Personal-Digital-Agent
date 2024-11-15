package online.dhbw_studentprojekt.msprefs.service;

import online.dhbw_studentprojekt.msprefs.model.Preference;
import online.dhbw_studentprojekt.msprefs.repository.PrefsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;


@ExtendWith(MockitoExtension.class)
class PrefsServiceTest {

    @Mock
    private PrefsRepository prefsRepository;

    @InjectMocks
    private PrefsService prefsService;

    @Test
    void getPref_shouldReturnPreferenceWhenFound() {
        String id = "testId";
        Preference preference = new Preference(id, List.of("value1", "value2"));
        Mockito.when(prefsRepository.findById(id)).thenReturn(Optional.of(preference));

        Optional<Preference> result = prefsService.getPref(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(preference.getValue().get(0), result.get().getValue().get(0));
        Assertions.assertEquals(preference.getValue().get(1), result.get().getValue().get(1));
    }

    @Test
    void createPref_shouldSavePreference() {
        online.dhbw_studentprojekt.dto.prefs.Preference dtoPref = new online.dhbw_studentprojekt.dto.prefs.Preference("testId", List.of("value1", "value2"));
        prefsService.createPref(dtoPref);

        ArgumentCaptor<Preference> captor = ArgumentCaptor.forClass(Preference.class);
        Mockito.verify(prefsRepository).save(captor.capture());
        Assertions.assertEquals("testId", captor.getValue().getId());
    }

    @Test
    void deletePref_shouldDeletePreference() {
        String id = "testId";
        prefsService.deletePref(id);
        Mockito.verify(prefsRepository).deleteById(id);
    }
}

