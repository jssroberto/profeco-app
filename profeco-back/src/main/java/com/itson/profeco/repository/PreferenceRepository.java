package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, UUID> {

}
