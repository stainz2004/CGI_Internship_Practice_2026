package org.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Seating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private SeatingType seatingType;
    private int maxPeople;
    @ManyToMany
    @JoinTable(
            name = "seating_seating_preference",
            joinColumns = @JoinColumn(name = "seating_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id")
    )
    private Set<SeatingPreference> preferences;
}
