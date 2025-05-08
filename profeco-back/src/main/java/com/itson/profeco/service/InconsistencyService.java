package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.repository.InconsistencyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InconsistencyService {

    private final InconsistencyRepository inconsistencyRepository;

    public List<Inconsistency> getAllInconsistencies() {
        return inconsistencyRepository.findAll();
    }

    public Inconsistency getInconsistencyById(UUID id) {
        return inconsistencyRepository.findById(id).orElse(null);
    }

    public Inconsistency saveInconsistency(Inconsistency inconsistency) {
        return inconsistencyRepository.save(inconsistency);
    }

    public Inconsistency updateInconsistency(UUID id, Inconsistency updatedInconsistency) {
        if (!inconsistencyRepository.existsById(id)) {
            return null;
        }
        updatedInconsistency.setId(id);
        return inconsistencyRepository.save(updatedInconsistency);
    }

    public void deleteInconsistency(UUID id) {
        inconsistencyRepository.deleteById(id);
    }
}
