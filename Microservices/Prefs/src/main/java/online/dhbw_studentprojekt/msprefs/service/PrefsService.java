package online.dhbw_studentprojekt.msprefs.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.msprefs.model.Preference;
import online.dhbw_studentprojekt.msprefs.repository.PrefsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
