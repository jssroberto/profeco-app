package com.itson.profeco.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity(name = "products")
public class Product {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, length = 250)
    private String brand;

    @Column(nullable = false)
    private Double price;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "product")
    private List<Inconsistency> inconsistencies;

    public Product() {
        this.inconsistencies = new ArrayList<>();
    }

    public Product(String name, String category, String brand, Double price, Store store, List<Inconsistency> inconsistencies) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.store = store;
        this.inconsistencies = inconsistencies;
    }
}
