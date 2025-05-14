package com.itson.profeco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itson.profeco.api.dto.request.SaveInconsistencyRequest;
import com.itson.profeco.exceptions.InvalidDataException;
import com.itson.profeco.exceptions.NotFoundException;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.service.InconsistencyService;

@RestController
public class InconcistencyController {
    
    private final InconsistencyService inconsistencyService;

    @Autowired
    public InconcistencyController(InconsistencyService inconsistencyService) {
        this.inconsistencyService = inconsistencyService;
    }

    @PostMapping()
    public Inconsistency save(SaveInconsistencyRequest request) throws InvalidDataException, NotFoundException {
        return this.inconsistencyService.save(request);
    }
}
