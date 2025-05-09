package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.util.UriComponentsBuilder;
import com.itson.profeco.api.dto.request.RoleRequest;
import com.itson.profeco.api.dto.response.RoleResponse;
import com.itson.profeco.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Operations related to Role")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create role", description = "Creates a new role.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "201", description = "Role created successfully")})
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        RoleResponse createdRole = roleService.createRole(roleRequest);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all roles", description = "Returns a list of all roles.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "List of roles returned successfully")})
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Get role by ID", description = "Returns a role by its ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")})
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        RoleResponse role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Update role", description = "Updates an existing role.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "201", description = "Role updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Role not found")})
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID id,
            @Valid @RequestBody RoleRequest roleRequest, UriComponentsBuilder uriBuilder) {
        RoleResponse updatedRole = roleService.updateRole(id, roleRequest);

        URI location = uriBuilder.path("/api/v1/roles/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(updatedRole);
    }

    @Operation(summary = "Delete role", description = "Deletes a role by its ID.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "204", description = "Role deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Role not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
