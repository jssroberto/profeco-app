package com.itson.profeco.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
