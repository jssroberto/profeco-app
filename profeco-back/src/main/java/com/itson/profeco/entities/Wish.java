package com.itson.profeco.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "wishs")
public class Wish {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid",  updatable = false, nullable = false)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
