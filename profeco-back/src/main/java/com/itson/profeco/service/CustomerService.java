package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Changed import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.mapper.CustomerMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final BCryptPasswordEncoder passwordEncoder; 

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllUsers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customerMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getUserById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Customer not found with id: " + id));
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        Customer customer = customerMapper.toEntity(customerRequest);
        // Encode the password before saving
        if (customer.getUser() != null && customerRequest.getPassword() != null) {
            customer.getUser().setPassword(passwordEncoder.encode(customerRequest.getPassword()));
        }
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        Customer updatedCustomer = customerMapper.toEntity(customerRequest);
        updatedCustomer.setId(id);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByUser_Email(email).orElseThrow(
                () -> new EntityNotFoundException("Customer not found for user email: " + email));
        return customerMapper.toResponse(customer);
    }
}
