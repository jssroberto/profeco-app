package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.request.UpdateInconsistencyStatusRequest;
import com.itson.profeco.exceptions.InvalidDataException;
import com.itson.profeco.exceptions.NotFoundException;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.service.InconsistencyService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/incosistencies")
@RequiredArgsConstructor
public class InconcistencyController {

    private final InconsistencyService inconsistencyService;

    @GetMapping()
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco'))")
    @SecurityRequirement(name = "bearerAuth")
    public List<Inconsistency> findAll() {
        return this.inconsistencyService.findAll();
    }

    @GetMapping("{id}") // Sin restricciones: acceso público
    public Inconsistency findById(@PathVariable UUID id) {
        return this.inconsistencyService.getById(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    @SecurityRequirement(name = "bearerAuth")
    public Inconsistency save(@RequestBody InconsistencyRequest request)
            throws InvalidDataException, NotFoundException {
        return this.inconsistencyService.save(request);
    }

    @PatchMapping()
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco'))")
    @SecurityRequirement(name = "bearerAuth")
    public Inconsistency update(@RequestBody UpdateInconsistencyStatusRequest request)
            throws NotFoundException {
        return this.inconsistencyService.update(request);
    }
}
