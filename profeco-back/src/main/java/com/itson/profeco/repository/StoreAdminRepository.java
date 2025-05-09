package com.itson.profeco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.StoreAdmin;

public interface StoreAdminRepository extends JpaRepository<StoreAdmin, UUID> {

    Optional<StoreAdmin> findByUser_Id(UUID userId);

}
