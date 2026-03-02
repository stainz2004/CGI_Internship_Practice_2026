package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.entity.Seating;
import org.example.backend.exception.SeatingNotFoundException;
import org.example.backend.mapper.SeatingMapper;
import org.example.backend.repository.SeatingRepository;
import org.example.backend.specification.SeatingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingService {

    private final SeatingRepository seatingRepository;
    private final SeatingMapper seatingMapper;

    public List<SeatingResponseDto> getAllSeating() {
        return seatingMapper.toResponseDto(seatingRepository.findAll());
    }

    public List<SeatingFilterResponseDto> getFilteredSeating(LocalDateTime dateAndTime, int numberOfPeople, Long seatingTypeId, Long seatingPreferenceId) {
        List<Seating> allSeatings = seatingRepository.findAll();

        System.out.println(seatingPreferenceId);
        Specification<Seating> spec = SeatingSpecification.matchesFilter(dateAndTime, numberOfPeople, seatingTypeId, seatingPreferenceId);
        List<Long> matchingIds = seatingRepository.findAll(spec)
                .stream()
                .map(Seating::getId)
                .toList();

        return allSeatings.stream()
                .map(seating -> {
                    SeatingFilterResponseDto dto = new SeatingFilterResponseDto();
                    dto.setId(seating.getId());
                    dto.setMatchesFilter(matchingIds.contains(seating.getId()));
                    return dto;
                })
                .toList();
    }

    public List<SeatingFilterResponseDto> getMostMatchingSeating(LocalDateTime dateAndTime, int numberOfPeople, Long seatingTypeId, Long seatingPreferenceId) {
        Specification<Seating> spec = SeatingSpecification.matchesFilter(dateAndTime, numberOfPeople, null, null);
        List<Seating> matchingSeatings = seatingRepository.findAll(spec);

        if (matchingSeatings.isEmpty()) {
            throw new SeatingNotFoundException("No matching seating found for the given filters");
        }

        Seating mostMatchingSeating = matchingSeatings.stream()
                .max(Comparator.comparingDouble(s -> calculateScore(s, numberOfPeople, seatingTypeId, seatingPreferenceId)))
                .orElseThrow();

        return List.of(seatingMapper.toFilterResponseDto(mostMatchingSeating));
    }

    public List<Long> getBookedSeatings(LocalDateTime dateAndTime) {
        if (dateAndTime == null) {
            throw new IllegalArgumentException("Date and time can not be empty!");
        }

        Specification<Seating> spec = SeatingSpecification.isBookedAt(dateAndTime);

        List<Seating> bookedSeatings = seatingRepository.findAll(spec);

        return bookedSeatings.stream().map(Seating::getId).toList();
    }

    private double calculateScore(Seating seating, int numberOfPeople, Long seatingTypeId, Long seatingPreferenceId) {
        double score = 9.0 * (numberOfPeople / (double) seating.getMaxPeople());
        if (seatingTypeId != null && seatingTypeId.equals(seating.getSeatingType().getId())) {
            score += 4;
        }
        return score;
    }
}
