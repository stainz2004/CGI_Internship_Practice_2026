package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AddReservationDto;
import org.example.backend.dto.ReservationResponseDto;
import org.example.backend.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody AddReservationDto addReservationDto) {
        return ResponseEntity.ok().body(reservationService.addReservation(addReservationDto));
    }
}
