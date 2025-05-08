package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Preference;
import com.itson.profeco.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public Preference getPreferenceById(UUID id) {
        return preferenceRepository.findById(id).orElse(null);
    }

    public Preference savePreference(Preference preference) {
        return preferenceRepository.save(preference);
    }

    public Preference updatePreference(UUID id, Preference updatedPreference) {
        if (!preferenceRepository.existsById(id)) {
            return null;
        }
        updatedPreference.setId(id);
        return preferenceRepository.save(updatedPreference);
    }

    public void deletePreference(UUID id) {
        preferenceRepository.deleteById(id);
    }
}
