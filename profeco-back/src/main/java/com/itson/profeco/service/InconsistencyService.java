package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.itson.profeco.api.dto.request.SaveInconsistencyRequest;
import com.itson.profeco.exceptions.InvalidDataException;
import com.itson.profeco.exceptions.NotFoundException;
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

    public List<Inconsistency> findAll() {
        return this.inconsistencyRepository.findAll();
    }

    public Inconsistency getById(UUID id) {
        return this.inconsistencyRepository.findById(id).orElse(null);
    }

    public Inconsistency save(SaveInconsistencyRequest inconsistency) throws InvalidDataException, NotFoundException {
        Customer customer = this.customerRepository.findByUser_Id(inconsistency.getCustomerId()).get();
        InconsistencyStatus status = this.inconsistencyStatusRepository.findByName(inconsistency.getStatus()).get();
        StoreProduct storeProduct = this.storeProductRepository.getReferenceById(inconsistency.getStoreProductUUID());
        Inconsistency newInconsistency = new Inconsistency();
        if (inconsistency.getActualPrice() == null) {
            throw new InvalidDataException("Se requiere el precio actual");
        }
        if (inconsistency.getDate() == null) {
            throw new InvalidDataException("Se requiere la fecha");
        }
        if (inconsistency.getPublishedPrice() == null) {
            throw new InvalidDataException("Se requiere el precio publicado");
        }
        if (inconsistency.getCustomerId() == null) {
            throw new InvalidDataException("Se requiere el id del cliente");
        }
        if (inconsistency.getStoreProductUUID() == null) {
            throw new InvalidDataException("Se requiere el id del StoreProduct");
        }
        if (inconsistency.getStatus() == null) {
            throw new InvalidDataException("Se requiere el status de la inconsistencia");
        }
        if (customer == null) {
            throw new NotFoundException("El cliente no existe en la base de datos");
        }
        if (status == null) {
            throw new NotFoundException("El estado no es válido");
        }
        if (storeProduct == null) {
            throw new NotFoundException("Store product no encontrado");
        }
        newInconsistency.setActualPrice(inconsistency.getActualPrice());
        newInconsistency.setPublishedPrice(inconsistency.getPublishedPrice());
        newInconsistency.setDate(inconsistency.getDate());
        newInconsistency.setStatus(status);
        newInconsistency.setCustomer(customer);
        newInconsistency.setStoreProduct(storeProduct);
        return this.inconsistencyRepository.save(newInconsistency);
    }

    public Inconsistency update(UUID id, Inconsistency updatedInconsistency) {
        if (!this.inconsistencyRepository.existsById(id)) {
            return null;
        }
        updatedInconsistency.setId(id);
        return this.inconsistencyRepository.save(updatedInconsistency);
    }
}
