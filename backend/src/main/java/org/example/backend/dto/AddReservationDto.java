package org.example.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddReservationDto {
    @NotNull(message = "Seating id is required")
    private Long seatingId;

    @NotNull(message = "Start time is required")
    @Future(message = "Reservation must be in the future")
    private LocalDateTime startTime;
}
