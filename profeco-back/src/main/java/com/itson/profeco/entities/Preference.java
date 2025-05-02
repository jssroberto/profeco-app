package com.itson.profeco.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "preferences")
public class Preference {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid",  updatable = false, nullable = false)
    private String id;
}
