package online.dhbw_studentprojekt.msprefs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.msprefs.model.Preference;
import online.dhbw_studentprojekt.msprefs.model.PreferenceForm;
import online.dhbw_studentprojekt.msprefs.repository.PrefsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrefsService {

    private final PrefsRepository prefsRepository;

    public Optional<Preference> getPref(String id) {

        return prefsRepository.findById(id);
    }

    public void createPref(online.dhbw_studentprojekt.dto.prefs.Preference preference) {

        online.dhbw_studentprojekt.msprefs.model.Preference pref = new Preference(preference.id(), preference.value());
        prefsRepository.save(pref);
    }

    public void deletePref(String id) {

        prefsRepository.deleteById(id);
    }

    public PreferenceForm getAllPrefs() {

        Optional<Preference> preferenceOptional = prefsRepository.findById("pref-form");

        if (preferenceOptional.isPresent()) {
            Preference preference = preferenceOptional.get();
            String jsonString = preference.getValue().getFirst();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonString, PreferenceForm.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to map JSON string to PreferenceForm", e);
            }
        } else {
            throw new RuntimeException("Preference form not found");
        }
    }

        public void setAllPrefs(String preferenceFormJSON) {
            Preference pref = new Preference("pref-form", List.of(preferenceFormJSON));

            prefsRepository.save(pref);

        }

}
