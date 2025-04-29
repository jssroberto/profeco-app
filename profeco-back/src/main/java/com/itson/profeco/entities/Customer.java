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

    public Customer() {
        this.ratings = new ArrayList<>();
    }

    public Customer(String name, String email, List<Rating> ratings) {
        this.name = name;
        this.email = email;
        this.ratings = ratings;
    }
}
