package com.itson.profeco.service;

import java.util.Set;
import java.util.UUID;
import com.itson.profeco.security.CustomUserDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.mapper.CustomerMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Role;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private static final String DEFAULT_USER_ROLE = "CUSTOMER";

    @Transactional(readOnly = true)
    public CustomerResponse getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("There is no authenticated user to get the current client.");
        }

        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("The authenticated user's email could not be determined.");
        }
        Customer customer = customerRepository.findByUser_Email(username)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Client not found for authenticated user: " + username );
                });
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        Customer customer = customerMapper.toEntity(customerRequest);
        UserEntity user = customer.getUser();

        if (user == null) {
            throw new IllegalStateException("User entity no está asociado con el CustomerRequest.");
        }

        if (customerRepository.findByUser_Email(user.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("A client with the email '" + user.getEmail() + "'already exists.");
        }

        if (customerRequest.getPassword() != null && !customerRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(customerRequest.getPassword()));
        } else {
            throw new IllegalArgumentException("The password cannot be empty for a new customer.");
        }

        Role defaultRole = roleService.getRoleEntityByName(DEFAULT_USER_ROLE);
        user.setRoles(Set.of(defaultRole));


        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));


        customerMapper.updateEntityFromRequest(customerRequest, existingCustomer);


        if (customerRequest.getPassword() != null && !customerRequest.getPassword().isEmpty()) {
            if (existingCustomer.getUser() != null) {
                existingCustomer.getUser().setPassword(passwordEncoder.encode(customerRequest.getPassword()));
            } else {

                throw new IllegalStateException("The password cannot be updated, the client does not have an associated user.");
            }
        }

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }


    // METÓDOS NO USADOS, EN LA CÁRCEL DE METODOS

//    @Transactional(readOnly = true)
//    public CustomerResponse getCustomerByEmail(String email) {
//        Customer customer = customerRepository.findByUser_Email(email).orElseThrow(
//                () -> new EntityNotFoundException("Customer not found for user email: " + email));
//        return customerMapper.toResponse(customer);
//    }
//
//    @Transactional(readOnly = true)
//    public List<CustomerResponse> getAllUsers() {
//        List<Customer> customers = customerRepository.findAll();
//        return customers.stream().map(customerMapper::toResponse).toList();
//    }
//
//    @Transactional(readOnly = true)
//    public CustomerResponse getUserById(UUID id) {
//        Customer customer = customerRepository.findById(id).orElseThrow(
//                () -> new EntityNotFoundException("Customer not found with id: " + id));
//        return customerMapper.toResponse(customer);
//    }

}