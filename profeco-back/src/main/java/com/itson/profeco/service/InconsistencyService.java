package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.request.UpdateInconsistencyStatusRequest;
import com.itson.profeco.exceptions.InvalidDataException;
import com.itson.profeco.exceptions.NotFoundException;
import com.itson.profeco.mapper.InconsistencyMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.InconsistencyStatus;
import com.itson.profeco.model.StoreProduct;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.InconsistencyRepository;
import com.itson.profeco.repository.InconsistencyStatusRepository;
import com.itson.profeco.repository.StoreProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InconsistencyService {

    private final InconsistencyRepository inconsistencyRepository;
    private final CustomerRepository customerRepository;
    private final InconsistencyStatusRepository inconsistencyStatusRepository;
    private final StoreProductRepository storeProductRepository;

    private final InconsistencyMapper inconsistencyMapper;

    public List<Inconsistency> findAll() {
        return this.inconsistencyRepository.findAll();
    }

    public Inconsistency getById(UUID id) {
        return this.inconsistencyRepository.findById(id).orElse(null);
    }

    public Inconsistency save(InconsistencyRequest inconsistency)
            throws InvalidDataException, NotFoundException {
        // Customer customer =
        //         this.customerRepository.findByUser_Id(inconsistency.getCustomerId()).get();
        // InconsistencyStatus status =
        //         this.inconsistencyStatusRepository.findByName(inconsistency.getStatus()).get();
        // StoreProduct storeProduct =
        //         this.storeProductRepository.getReferenceById(inconsistency.getStoreProductUUID());
        // Inconsistency newInconsistency = new Inconsistency();

        // newInconsistency.setActualPrice(inconsistency.getActualPrice());
        // newInconsistency.setPublishedPrice(inconsistency.getPublishedPrice());
        // newInconsistency.setDate(inconsistency.getDate());
        // newInconsistency.setStatus(status);
        // newInconsistency.setCustomer(customer);
        // newInconsistency.setStoreProduct(storeProduct);
        // return this.inconsistencyRepository.save(newInconsistency);
        return null;
    }

    public Inconsistency update(UpdateInconsistencyStatusRequest request) throws NotFoundException {
        InconsistencyStatus status =
                this.inconsistencyStatusRepository.findByName(request.getStatus()).get();
        if (status == null) {
            throw new NotFoundException("Estado de inconsitencia inválido");
        }
        Inconsistency inconsistency =
                this.inconsistencyRepository.getReferenceById(request.getUuid());

        inconsistency.setStatus(status);
        return this.inconsistencyRepository.save(inconsistency);
    }
}
