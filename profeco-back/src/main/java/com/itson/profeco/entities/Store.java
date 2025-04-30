package com.itson.profeco.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity(name = "stores")
public class Store {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid",  updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String location;

    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToMany(mappedBy = "store")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "store")
    private List<Inconsistency> inconsistencies;

    public Store() {
        this.products = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.inconsistencies = new ArrayList<>();
    }

    public Store(String name, String location, List<Product> products, List<Rating> ragints, List<Inconsistency> inconsistencies) {
        this.name = name;
        this.location = location;
        this.products = products;
        this.ratings = ragints;
        this.inconsistencies = inconsistencies;
    }
}
