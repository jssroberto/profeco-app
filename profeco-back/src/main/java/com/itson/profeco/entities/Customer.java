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
@Entity(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 250)
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "customer")
    private List<Inconsistency> inconsistencies;

    @OneToMany(mappedBy = "customer")
    private List<Wish> wishs;

    public Customer() {
        this.ratings = new ArrayList<>();
        this.inconsistencies = new ArrayList<>();
        this.wishs = new ArrayList<>();
    }

    public Customer(String name, String email, List<Rating> ratings, List<Wish> wishs) {
        this.name = name;
        this.email = email;
        this.ratings = ratings;
        this.wishs = wishs;
    }
}
