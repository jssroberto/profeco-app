package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.RoleRequest;
import com.itson.profeco.api.dto.response.RoleResponse;
import com.itson.profeco.mapper.RoleMapper;
import com.itson.profeco.model.Role;
import com.itson.profeco.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        role = roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Role not found with name: " + name));
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public Role getRoleEntityByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Role not found with name: " + name));
    }

    @Transactional
    public RoleResponse updateRole(UUID id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        existingRole.setName(roleRequest.getName());
        existingRole = roleRepository.save(existingRole);
        return roleMapper.toResponse(existingRole);
    }

    @Transactional
    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}
