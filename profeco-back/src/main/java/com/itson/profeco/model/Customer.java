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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 250, unique = true)
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Inconsistency> inconsistencies = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Wish> wishes = new ArrayList<>();
}
