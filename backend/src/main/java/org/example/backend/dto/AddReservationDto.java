package org.example.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddReservationDto {
    private Long seatingId;
    private LocalDateTime startTime;
}
