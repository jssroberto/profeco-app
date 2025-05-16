package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.api.dto.response.StoreAdminResponse;
import com.itson.profeco.mapper.InconsistencyMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.InconsistencyStatus;
import com.itson.profeco.model.StoreProduct; // Added import
import com.itson.profeco.repository.InconsistencyRepository;
import com.itson.profeco.repository.InconsistencyStatusRepository;
import com.itson.profeco.repository.StoreProductRepository;
import com.itson.profeco.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InconsistencyService {

    private final InconsistencyRepository inconsistencyRepository;
    private final StoreProductRepository storeProductRepository;
    private final InconsistencyStatusRepository inconsistencyStatusRepository;
    private final StoreRepository storeRepository;

    private final CustomerService customerService;
    private final StoreAdminService storeAdminService;
    private final ProfecoAdminService profecoAdminService;

    private final InconsistencyMapper inconsistencyMapper;

    @Value("${inconsistency.status.open}")
    private String defaultStatusOpen;

    @Value("${inconsistency.status.closed}")
    private String defaultStatusClosed;

    @Transactional(readOnly = true)
    public List<InconsistencyResponse> getInconsistenciesByCurrentCustomer() {
        Customer customer = customerService.getAuthenticatedCustomerEntity();
        if (customer == null) {
            throw new IllegalStateException("No authenticated customer found.");
        }
        List<Inconsistency> inconsistencies =
                inconsistencyRepository.findByCustomerId(customer.getId());
        return inconsistencies.stream().map(inconsistencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InconsistencyResponse> getInconsistenciesByCurrentStoreAdmin() {
        StoreAdminResponse currentStoreAdmin = storeAdminService.getCurrentStoreAdmin();
        if (currentStoreAdmin == null || currentStoreAdmin.getStoreId() == null) {
            throw new IllegalStateException(
                    "Authenticated store admin must be associated with a store.");
        }
        UUID storeId = currentStoreAdmin.getStoreId();
        List<Inconsistency> inconsistencies =
                inconsistencyRepository.findByStoreProductStoreId(storeId);
        return inconsistencies.stream().map(inconsistencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InconsistencyResponse> getAllInconsistenciesForProfecoAdmin() {
        profecoAdminService.getCurrentProfecoAdmin();
        List<Inconsistency> inconsistencies = inconsistencyRepository.findAll();
        return inconsistencies.stream().map(inconsistencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InconsistencyResponse getInconsistencyById(UUID id) {
        Inconsistency inconsistency = inconsistencyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Inconsistency not found with id: " + id));
        return inconsistencyMapper.toResponse(inconsistency);
    }

    @Transactional(readOnly = true)
    public List<InconsistencyResponse> getInconsistenciesByStoreProduct(UUID storeProductId) {
        storeProductRepository.findById(storeProductId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "StoreProduct not found with id: " + storeProductId));
        List<Inconsistency> inconsistencies =
                inconsistencyRepository.findByStoreProductId(storeProductId);
        return inconsistencies.stream().map(inconsistencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InconsistencyResponse> getInconsistenciesByStore(UUID storeId) {
        storeRepository.findById(storeId).orElseThrow(
                () -> new EntityNotFoundException("Store not found with id: " + storeId));
        List<Inconsistency> inconsistencies =
                inconsistencyRepository.findByStoreProductStoreId(storeId);
        return inconsistencies.stream().map(inconsistencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InconsistencyResponse saveInconsistency(InconsistencyRequest request) {
        Customer customer = customerService.getAuthenticatedCustomerEntity();
        if (customer == null) {
            throw new IllegalStateException(
                    "No authenticated customer found to report inconsistency.");
        }

        StoreProduct storeProduct = storeProductRepository.findById(request.getStoreProductId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "StoreProduct not found with id: " + request.getStoreProductId()));

        InconsistencyStatus pendingStatus = inconsistencyStatusRepository
                .findByName(defaultStatusOpen).orElseThrow(() -> new EntityNotFoundException(
                        "Default inconsistency status '" + defaultStatusOpen + "' not found."));

        Inconsistency inconsistency = inconsistencyMapper.toEntity(request);
        inconsistency.setCustomer(customer);
        inconsistency.setStatus(pendingStatus);
        inconsistency.setStoreProduct(storeProduct);
        if (storeProduct.getPrice() != null)
            inconsistency.setPublishedPrice(storeProduct.getPrice().doubleValue());

        Inconsistency savedInconsistency = inconsistencyRepository.save(inconsistency);
        return inconsistencyMapper.toResponse(savedInconsistency);
    }

    @Transactional
    public InconsistencyResponse updateInconsistencyStatus(UUID inconsistencyId) {
        Inconsistency inconsistency = inconsistencyRepository.findById(inconsistencyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Inconsistency not found with id: " + inconsistencyId));

        if (inconsistency.getStatus().getName().equalsIgnoreCase(defaultStatusClosed)) {
            throw new IllegalStateException("Inconsistency is already closed.");
        }

        InconsistencyStatus closedStatus =
                inconsistencyStatusRepository.findByName(defaultStatusClosed.toUpperCase())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "InconsistencyStatus not found with name: "
                                        + defaultStatusClosed.toUpperCase()));

        inconsistency.setStatus(closedStatus);
        Inconsistency updatedInconsistency = inconsistencyRepository.save(inconsistency);
        return inconsistencyMapper.toResponse(updatedInconsistency);
    }

    @Transactional
    public void deleteInconsistency(UUID id) {
        if (!inconsistencyRepository.existsById(id)) {
            throw new EntityNotFoundException("Inconsistency not found with id: " + id);
        }
        inconsistencyRepository.deleteById(id);
    }

}
