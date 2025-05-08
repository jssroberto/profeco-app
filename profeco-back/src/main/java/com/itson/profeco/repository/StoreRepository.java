package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Store;

public interface StoreRepository extends JpaRepository<Store, UUID> {

}
