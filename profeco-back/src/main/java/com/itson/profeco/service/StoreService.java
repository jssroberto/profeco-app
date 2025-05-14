package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.StoreRequest;
import com.itson.profeco.api.dto.response.StoreResponse;
import com.itson.profeco.mapper.StoreMapper;
import com.itson.profeco.model.Store;
import com.itson.profeco.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Transactional(readOnly = true)
    public List<StoreResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(storeMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public StoreResponse getStoreById(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + id));
        return storeMapper.toResponse(store);
    }

    @Transactional
    public StoreResponse saveStore(StoreRequest storeRequest, String imageUrl) {
        Store store = storeMapper.toEntity(storeRequest);
        store.setImageUrl(imageUrl);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toResponse(savedStore);
    }

    @Transactional
    public StoreResponse updateStore(UUID id, StoreRequest storeRequest) {
        if (!storeRepository.existsById(id)) {
            throw new EntityNotFoundException("Store not found with id: " + id);
        }
        Store storeToUpdate = storeMapper.toEntity(storeRequest);
        storeToUpdate.setId(id);
        Store updatedStore = storeRepository.save(storeToUpdate);
        return storeMapper.toResponse(updatedStore);
    }

    @Transactional
    public void deleteStore(UUID id) {
        if (!storeRepository.existsById(id)) {
            throw new EntityNotFoundException("Store not found with id: " + id);
        }
        storeRepository.deleteById(id);
    }
}
