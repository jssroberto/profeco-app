package com.itson.profeco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.InconsistencyStatus;

public interface InconsistencyStatusRepository extends JpaRepository<InconsistencyStatus, UUID> {

    Optional<InconsistencyStatus> findByName(String name);
}
