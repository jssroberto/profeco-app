package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itson.profeco.api.dto.request.SaveInconsistencyRequest;
import com.itson.profeco.api.dto.request.UpdateInconsistencyStatusRequest;
import com.itson.profeco.exceptions.InvalidDataException;
import com.itson.profeco.exceptions.NotFoundException;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.service.InconsistencyService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("api/v1/incosistencies")
public class InconcistencyController {
    
    private final InconsistencyService inconsistencyService;

    @Autowired
    public InconcistencyController(InconsistencyService inconsistencyService) {
        this.inconsistencyService = inconsistencyService;
    }

    @GetMapping()
    public List<Inconsistency> findAll() {
        return this.inconsistencyService.findAll();
    }

    @GetMapping("{id}")
    public Inconsistency findById(@PathVariable UUID id) {
        return this.inconsistencyService.getById(id);   
    }

    @PostMapping()
    @PreAuthorize("hasRole('CUSTOMER')") // TODO: Needs to be ROLE_CUSTOMER
    public Inconsistency save(@RequestBody SaveInconsistencyRequest request) throws InvalidDataException, NotFoundException {
        return this.inconsistencyService.save(request);
    }

    @PatchMapping()
    public Inconsistency update(@RequestBody UpdateInconsistencyStatusRequest request) throws NotFoundException {
        return this.inconsistencyService.update(request);
    }
}
