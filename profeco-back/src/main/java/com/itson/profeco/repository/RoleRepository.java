package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

}
