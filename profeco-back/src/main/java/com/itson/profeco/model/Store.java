package com.itson.profeco.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "store")
    private List<StoreProduct> products = new ArrayList<>();

    @OneToMany(mappedBy = "store") 
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Inconsistency> inconsistencies = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Wish> wishes = new ArrayList<>();

}
