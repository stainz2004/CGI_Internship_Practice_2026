package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/seating")
@RequiredArgsConstructor
public class SeatingController {

    private final SeatingService seatingService;

    @GetMapping()
    public ResponseEntity<List<SeatingResponseDto>> getAllSeating() {
        return ResponseEntity.ok().body(seatingService.getAllSeating());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SeatingResponseDto>> getAllSeatingFilter(@RequestParam (required = false) LocalDateTime dateAndTime,
                                                                        @RequestParam (required = false)int numberOfPeople,
                                                                        @RequestParam (required = false)Long seatingTypeId) {
        return ResponseEntity.ok().body(seatingService.getAllSeating());
    }
}
