package com.itson.profeco.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProfecoAdminRequest;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.mapper.ProfecoAdminMapper;
import com.itson.profeco.model.ProfecoAdmin;
import com.itson.profeco.model.Role;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.ProfecoAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfecoAdminService {

    private final ProfecoAdminRepository profecoAdminRepository;
    private final ProfecoAdminMapper profecoAdminMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private static final String DEFAULT_USER_ROLE = "PROFECO_ADMIN";

    @Transactional(readOnly = true)
    public ProfecoAdminResponse getCurrentProfecoAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No authenticated admin user found");
        }

        String username = authentication.getName();
        ProfecoAdmin admin = profecoAdminRepository.findByUser_Email(username).orElseThrow(
                () -> new EntityNotFoundException("Profeco admin not found for user: " + username));

        return profecoAdminMapper.toResponse(admin);
    }

    @Transactional
    public ProfecoAdminResponse saveProfecoAdmin(ProfecoAdminRequest request) {
        if (profecoAdminRepository.findByUser_Email(request.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException(
                    "Profeco admin with email " + request.getEmail() + " already exists");
        }
        ProfecoAdmin admin = profecoAdminMapper.toEntity(request);
        UserEntity user = admin.getUser();

        if (user == null) {
            throw new IllegalStateException("User entity not created for profeco admin");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role adminRole = roleService.getRoleEntityByName(DEFAULT_USER_ROLE);
        user.setRoles(Set.of(adminRole));

        return profecoAdminMapper.toResponse(profecoAdminRepository.save(admin));
    }

    @Transactional
    public ProfecoAdminResponse updateProfecoAdmin(UUID id, ProfecoAdminRequest request) {
        ProfecoAdmin existingAdmin = profecoAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Profeco admin not found with id: " + id));

        profecoAdminMapper.updateEntityFromRequest(request, existingAdmin);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingAdmin.getUser().setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return profecoAdminMapper.toResponse(profecoAdminRepository.save(existingAdmin));
    }

    @Transactional
    public void deleteProfecoAdmin(UUID id) {
        ProfecoAdmin admin = profecoAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProfecoAdmin not found with id: " + id));

        UserEntity user = admin.getUser();
        profecoAdminRepository.delete(admin);
    }

    // cárcel de métodos
    // @Transactional(readOnly = true)
    // public ProfecoAdminResponse getProfecoAdminByEmail(String email) {
    // return profecoAdminMapper.toResponse(
    // profecoAdminRepository.findByUser_Email(email)
    // .orElseThrow(() -> new EntityNotFoundException("ProfecoAdmin not found with email: " +
    // email))
    // );
    // }
    //
    //
    // @Transactional(readOnly = true)
    // public List<ProfecoAdminResponse> getAllProfecoAdmins() {
    // return profecoAdminRepository.findAll().stream()
    // .map(profecoAdminMapper::toResponse)
    // .toList();
    // }
    //
    // @Transactional(readOnly = true)
    // public ProfecoAdminResponse getProfecoAdminById(UUID id) {
    // return profecoAdminMapper.toResponse(
    // profecoAdminRepository.findById(id)
    // .orElseThrow(() -> new EntityNotFoundException("ProfecoAdmin not found with id: " + id))
    // );
    // }

}
