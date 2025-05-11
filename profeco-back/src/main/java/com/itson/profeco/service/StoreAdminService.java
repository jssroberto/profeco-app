package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.StoreAdminRequest;
import com.itson.profeco.api.dto.response.StoreAdminResponse;
import com.itson.profeco.mapper.StoreAdminMapper;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreAdmin;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.StoreAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreAdminService {

    private final StoreAdminRepository storeAdminRepository;
    private final StoreAdminMapper storeAdminMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<StoreAdminResponse> getAllStoreAdmins() {
        List<StoreAdmin> storeAdmins = storeAdminRepository.findAll();
        return storeAdmins.stream().map(storeAdminMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public StoreAdminResponse getStoreAdminById(UUID id) {
        StoreAdmin storeAdmin = storeAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("StoreAdmin not found with id: " + id));
        return storeAdminMapper.toResponse(storeAdmin);
    }

    @Transactional
    public StoreAdminResponse saveStoreAdmin(StoreAdminRequest storeAdminRequest) {
        StoreAdmin storeAdmin = storeAdminMapper.toEntity(storeAdminRequest);

        // Encode the password before saving
        if (storeAdmin.getUser() != null && storeAdmin.getUser().getPassword() != null) {
            storeAdmin.getUser()
                    .setPassword(passwordEncoder.encode(storeAdmin.getUser().getPassword()));
        }

        StoreAdmin savedStoreAdmin = storeAdminRepository.save(storeAdmin);
        return storeAdminMapper.toResponse(savedStoreAdmin);
    }

    @Transactional
    public StoreAdminResponse updateStoreAdmin(UUID id, StoreAdminRequest storeAdminRequest) {
        StoreAdmin storeAdmin = storeAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("StoreAdmin not found with id: " + id));

        storeAdmin.setName(storeAdminRequest.getName());

        UserEntity user = storeAdmin.getUser();
        if (user == null) {
            // This case might indicate an inconsistency or a need for different handling
            // For now, we assume a user always exists for a store admin.
            throw new IllegalStateException("User entity not found for StoreAdmin with id: " + id);
        }

        user.setEmail(storeAdminRequest.getEmail());

        if (storeAdminRequest.getPassword() != null && !storeAdminRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(storeAdminRequest.getPassword()));
        }

        if (storeAdminRequest.getStoreId() != null) {
            Store store = new Store();
            store.setId(storeAdminRequest.getStoreId());
            storeAdmin.setStore(store);
        } else {
            storeAdmin.setStore(null); // Or handle as per business logic, e.g., disallow unsetting
        }

        StoreAdmin updatedStoreAdmin = storeAdminRepository.save(storeAdmin);
        return storeAdminMapper.toResponse(updatedStoreAdmin);
    }

    @Transactional
    public void deleteStoreAdmin(UUID id) {
        if (!storeAdminRepository.existsById(id)) {
            throw new EntityNotFoundException("StoreAdmin not found with id: " + id);
        }
        storeAdminRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StoreAdminResponse getStoreAdminByEmail(String email) {
        StoreAdmin storeAdmin = storeAdminRepository.findByUser_Email(email).orElseThrow(
                () -> new EntityNotFoundException("StoreAdmin not found for user email: " + email));
        return storeAdminMapper.toResponse(storeAdmin);
    }
}
