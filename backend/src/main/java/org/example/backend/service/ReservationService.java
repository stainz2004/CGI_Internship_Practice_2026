package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AddReservationDto;
import org.example.backend.dto.ReservationResponseDto;
import org.example.backend.entity.Reservation;
import org.example.backend.repository.ReservationRepository;
import org.example.backend.repository.SeatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatingRepository seatingRepository;

    @Transactional
    public ReservationResponseDto addReservation(AddReservationDto addReservationDto) {
        Reservation reservation = new Reservation();
        reservation.setSeating(seatingRepository.getReferenceById(addReservationDto.getSeatingId()));
        reservation.setStartTime(addReservationDto.getStartTime());
        reservation.setEndTime(addReservationDto.getStartTime().plusHours(3));
        reservationRepository.save(reservation);
        return new ReservationResponseDto(true);
    }
}
