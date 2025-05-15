package com.itson.profeco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.UserEntity;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByUser_Id(UUID user_Id);

    Optional<Customer> findByUser_Email(String email);

    Optional<Customer> findByUser(UserEntity user);

}
