package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operations related to Customer")
public class CustomerController {

    private final CustomerService costumerService;

    @Operation(summary = "Get all customers", description = "Returns a list of all customers.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "List of customers returned successfully")})
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> responses = costumerService.getAllCustomers();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get customer by ID", description = "Returns a customer by their ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        CustomerResponse response = costumerService.getCustomerById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create customer", description = "Creates a new customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully")})
    @PostMapping
    public ResponseEntity<CustomerResponse> saveCustomer(
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = costumerService.saveCustomer(customerRequest);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id,
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = costumerService.updateCustomer(id, customerRequest);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete customer", description = "Deletes a customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        costumerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
