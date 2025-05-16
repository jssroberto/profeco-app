package com.itson.profeco.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.mapper.CustomerMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Preference;
import com.itson.profeco.model.Role;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.UserRepository;
import com.itson.profeco.security.CustomUserDetails;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Value("${role.customer}")
    private String defaultUserRole;

    public Customer getAuthenticatedCustomerEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findById(userDetails.getGenericUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with ID: " + userDetails.getGenericUserId()));

        return customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(
                "Customer not found for authenticated user: " + userDetails.getUsername()));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No hay un usuario autenticado");
        }

        String username = authentication.getName();
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException(
                    "No se pudo determinar el email del usuario autenticado");
        }

        Customer customer = customerRepository.findByUser_Email(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "StoreAdmin no encontrado para el usuario: " + username));

        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCurrentCustomer(CustomerRequest customerRequest) {
        Customer existingCustomer = getAuthenticatedCustomerEntity();
        customerMapper.updateEntityFromRequest(customerRequest, existingCustomer);
        if (customerRequest.getPassword() != null && !customerRequest.getPassword().isEmpty()) {
            UserEntity user = existingCustomer.getUser();
            if (user != null) {
                user.setPassword(passwordEncoder.encode(customerRequest.getPassword()));
            } else {
                throw new IllegalStateException(
                        "The password cannot be updated, the customer does not have an associated user account.");
            }
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCurrentCustomer() {
        Customer customerToDelete = getAuthenticatedCustomerEntity();
        customerRepository.delete(customerToDelete);
    }

    @Transactional
    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        Customer customer = customerMapper.toEntity(customerRequest);
        UserEntity user = customer.getUser();

        if (customerRepository.findByUser_Email(user.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException(
                    "A customer with the email '" + user.getEmail() + "' already exists.");
        }

        Role defaultRole = roleService.getRoleEntityByName(defaultUserRole);
        user.setRoles(Set.of(defaultRole));

        Preference preference = new Preference();
        preference.setCustomer(customer);
        customer.setPreference(preference);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

}
