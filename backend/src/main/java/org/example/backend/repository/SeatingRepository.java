package org.example.backend.repository;

import org.example.backend.entity.Seating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingRepository extends JpaRepository<Seating, Long> {
}
