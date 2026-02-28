package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.entity.Seating;
import org.example.backend.mapper.SeatingMapper;
import org.example.backend.repository.SeatingRepository;
import org.example.backend.specification.SeatingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SeatingService {

    private final SeatingRepository seatingRepository;
    private final SeatingMapper seatingMapper;

    public List<SeatingResponseDto> getAllSeating() {
        return seatingMapper.toResponseDto(seatingRepository.findAll());
    }

    public List<SeatingFilterResponseDto> getFilteredSeating(LocalDateTime dateAndTime, int numberOfPeople, Long seatingTypeId) {
        List<Seating> allSeatings = seatingRepository.findAll();

        Specification<Seating> spec = SeatingSpecification.matchesFilter(dateAndTime, numberOfPeople, seatingTypeId);
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

    public List<SeatingFilterResponseDto> getMostMatchingSeating(LocalDateTime dateAndTime, int numberOfPeople, Long seatingTypeId) {
        Specification<Seating> spec = SeatingSpecification.matchesFilter(dateAndTime, numberOfPeople, null);
        List<Seating> matchingSeatings = seatingRepository.findAll(spec);

        if (matchingSeatings.isEmpty()) {
            throw new NoSuchElementException("No matching seating found for the given filters");
        }

        Seating mostMatchingSeating = matchingSeatings.stream()
                .max(Comparator.comparingDouble(s -> calculateScore(s, numberOfPeople, seatingTypeId)))
                .orElseThrow();

        return List.of(seatingMapper.toFilterResponseDto(mostMatchingSeating));
    }

    private double calculateScore(Seating seating, int numberOfPeople, Long seatingTypeId) {
        double score = 9.0 * (numberOfPeople / (double) seating.getMaxPeople());
        if (seatingTypeId != null && seatingTypeId.equals(seating.getSeatingType().getId())) {
            score += 4;
        }
        return score;
    }
}
