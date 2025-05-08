package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Inconsistency;

public interface InconsistencyRepository extends JpaRepository<Inconsistency, UUID> {

}
