package online.dhbw_studentprojekt.msprefs.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.msprefs.repository.PrefsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PrefsService {

    private final PrefsRepository prefsRepository;

    public Preference getPref(String id) {
        return prefsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).toDto();
    }


    public void createPref(Preference preference) {
        online.dhbw_studentprojekt.msprefs.model.Preference pref = new online.dhbw_studentprojekt.msprefs.model.Preference(preference.id(), preference.value());
        prefsRepository.save(pref);
    }

    public void deletePref(String id) {
        prefsRepository.deleteById(id);
    }

}
