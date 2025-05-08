package com.itson.profeco.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.model.Customer;
import com.itson.profeco.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operations related to Customer")
public class CustomerController {

    private final CustomerService costumerService;

    // TODO: We gotta change the Entity to CustomerResponse
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> responses = costumerService.getAllCustomers();
        return ResponseEntity.ok(responses);
    }


}
