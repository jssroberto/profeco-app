package com.itson.profeco.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Inconsistency {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid",  updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private Double publishedPrice;

    @Column(nullable = false)
    private Double actualPrice;

    @Column(nullable = false)
    private LocalDate date;

    // TODO: We need to work on how we're going to manage the states
    // TODO: because I'm not convinced by the idea of doing it because
    // TODO: Roberto's balls are swollen...

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;
}
