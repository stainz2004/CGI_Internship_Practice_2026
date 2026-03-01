package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.service.SeatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/seating/")
@RequiredArgsConstructor
public class SeatingController {

    private final SeatingService seatingService;

    @GetMapping()
    public ResponseEntity<List<SeatingResponseDto>> getAllSeating() {
        return ResponseEntity.ok().body(seatingService.getAllSeating());
    }

    @GetMapping("filter")
    public ResponseEntity<List<SeatingFilterResponseDto>> getAllSeatingFilter(@RequestParam(required = false) LocalDateTime dateAndTime,
                                                                              @RequestParam(required = false) Integer numberOfPeople,
                                                                              @RequestParam(required = false) Long seatingTypeId) {
        return ResponseEntity.ok().body(seatingService.getFilteredSeating(dateAndTime, numberOfPeople, seatingTypeId));
    }

    @GetMapping("filter/suggest")
    public ResponseEntity<List<SeatingFilterResponseDto>> getMostMatchingSeating(@RequestParam(required = false) LocalDateTime dateAndTime,
                                                                           @RequestParam(required = false) Integer numberOfPeople,
                                                                           @RequestParam(required = false) Long seatingTypeId) {
        return ResponseEntity.ok().body(seatingService.getMostMatchingSeating(dateAndTime, numberOfPeople, seatingTypeId));
    }

    @GetMapping("filter/booked")
    public ResponseEntity<List<Long>> getBookedSeatings(@RequestParam LocalDateTime dateAndTime) {
        return ResponseEntity.ok().body(seatingService.getBookedSeatings(dateAndTime));
    }

}
