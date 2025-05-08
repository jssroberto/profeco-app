package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.mapper.CustomerMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customerMapper::toResponse).toList();
    }

    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse saveCustomer(CustomerRequest customerRequest) {
        Customer customer = customerMapper.toEntity(customerRequest);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer updatedCustomer = customerMapper.toEntity(customerRequest);
        if (!customerRepository.existsById(id)) {
            return null;
        }
        updatedCustomer.setId(id);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toResponse(savedCustomer);
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
}
