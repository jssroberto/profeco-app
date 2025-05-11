package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProfecoAdminRequest;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.mapper.ProfecoAdminMapper;
import com.itson.profeco.model.ProfecoAdmin;
import com.itson.profeco.repository.ProfecoAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfecoAdminService {

    private final ProfecoAdminRepository profecoAdminRepository;
    private final ProfecoAdminMapper profecoAdminMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<ProfecoAdminResponse> getAllProfecoAdmins() {
        List<ProfecoAdmin> profecoAdmins = profecoAdminRepository.findAll();
        return profecoAdmins.stream().map(profecoAdminMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfecoAdminResponse getProfecoAdminById(UUID id) {
        ProfecoAdmin profecoAdmin = profecoAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProfecoAdmin not found with id: " + id));
        return profecoAdminMapper.toResponse(profecoAdmin);
    }

    @Transactional
    public ProfecoAdminResponse saveProfecoAdmin(ProfecoAdminRequest profecoAdminRequest) {
        ProfecoAdmin profecoAdmin = profecoAdminMapper.toEntity(profecoAdminRequest);
        // Encode the password before saving
        if (profecoAdmin.getUser() != null && profecoAdminRequest.getPassword() != null) {
            profecoAdmin.getUser()
                    .setPassword(passwordEncoder.encode(profecoAdminRequest.getPassword()));
        }
        ProfecoAdmin savedProfecoAdmin = profecoAdminRepository.save(profecoAdmin);
        return profecoAdminMapper.toResponse(savedProfecoAdmin);
    }

    @Transactional
    public ProfecoAdminResponse updateProfecoAdmin(UUID id,
            ProfecoAdminRequest profecoAdminRequest) {
        ProfecoAdmin profecoAdminToUpdate = profecoAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProfecoAdmin not found with id: " + id));

        // Update fields from request
        profecoAdminToUpdate.setName(profecoAdminRequest.getName());
        if (profecoAdminToUpdate.getUser() != null) {
            profecoAdminToUpdate.getUser().setEmail(profecoAdminRequest.getEmail());
            // Optionally update password if provided and not blank
            if (profecoAdminRequest.getPassword() != null
                    && !profecoAdminRequest.getPassword().isBlank()) {
                profecoAdminToUpdate.getUser()
                        .setPassword(passwordEncoder.encode(profecoAdminRequest.getPassword()));
            }
        }

        ProfecoAdmin updatedProfecoAdmin = profecoAdminRepository.save(profecoAdminToUpdate);
        return profecoAdminMapper.toResponse(updatedProfecoAdmin);
    }

    @Transactional
    public void deleteProfecoAdmin(UUID id) {
        if (!profecoAdminRepository.existsById(id)) {
            throw new EntityNotFoundException("ProfecoAdmin not found with id: " + id);
        }
        profecoAdminRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProfecoAdminResponse getProfecoAdminByEmail(String email) {
        ProfecoAdmin profecoAdmin = profecoAdminRepository.findByUser_Email(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProfecoAdmin not found for user email: " + email));
        return profecoAdminMapper.toResponse(profecoAdmin);
    }
}
