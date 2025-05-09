package com.itson.profeco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.ProfecoAdmin;

public interface ProfecoAdminRepository extends JpaRepository<ProfecoAdmin, UUID> {

    Optional<ProfecoAdmin> findByUser_Id(UUID userId);

}
