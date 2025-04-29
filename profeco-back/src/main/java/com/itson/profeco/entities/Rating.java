package com.itson.profeco.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "ratings")
public class Rating {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Integer score;

    @Column(length = 250, nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
