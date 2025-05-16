package com.itson.profeco.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private BigDecimal offerPrice;

    @Column(nullable = true)
    private LocalDate offerStartDate;

    @Column(nullable = true)
    private LocalDate offerEndDate;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Product product;

    @OneToMany(mappedBy = "storeProduct", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Inconsistency> inconsistencies = new ArrayList<>();
}
