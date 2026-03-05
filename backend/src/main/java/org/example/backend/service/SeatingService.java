package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingFilterDto;
import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.entity.Seating;
import org.example.backend.entity.SeatingPreference;
import org.example.backend.exception.SeatingNotFoundException;
import org.example.backend.mapper.SeatingMapper;
import org.example.backend.repository.SeatingRepository;
import org.example.backend.specification.SeatingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingService {

    private final SeatingRepository seatingRepository;
    private final SeatingMapper seatingMapper;

    /**
     * Returns all seatings in the system.
     */
    public List<SeatingResponseDto> getAllSeating() {
        return seatingMapper.toResponseDto(seatingRepository.findAll());
    }

    /**
     * Returns seatings that match the given filter criteria.
     */
    public List<SeatingFilterResponseDto> getFilteredSeating(SeatingFilterDto filterDTo) {

        Specification<Seating> spec = SeatingSpecification.matchesFilter(filterDTo.getDateAndTime(), filterDTo.getNumberOfPeople(), filterDTo.getSeatingTypeId(), filterDTo.getSelectedPreference());
        List<Seating> matchingSeatings = seatingRepository.findAll(spec);

        return seatingMapper.toFilterResponseDto(matchingSeatings);
    }

    /**
     * Finds the best matching seating(s) based on scoring logic.
     *
     * The algorithm assigns a score to each available seating and
     */
    public List<SeatingFilterResponseDto> getMostMatchingSeating(SeatingFilterDto filterDTo) {
        LocalDateTime dateAndTime = filterDTo.getDateAndTime();
        int numberOfPeople = filterDTo.getNumberOfPeople();
        Long seatingTypeId = filterDTo.getSeatingTypeId();
        Long seatingPreferenceId = filterDTo.getSelectedPreference();

        Specification<Seating> spec = SeatingSpecification.matchesFilter(dateAndTime, numberOfPeople, null, null);
        List<Seating> matchingSeatings = seatingRepository.findAll(spec);

        if (matchingSeatings.isEmpty()) {
            throw new SeatingNotFoundException("No matching seating found for the given filters");
        }

        // Calculate the highest score among the candidate seatings
        double maxScore = matchingSeatings.stream()
                .mapToDouble(s -> calculateScore(s, numberOfPeople, seatingTypeId, seatingPreferenceId))
                .max()
                .orElseThrow();

        // Return all seatings that share the highest score
        List<Seating> mostMatchingSeatings = matchingSeatings.stream()
                .filter(s -> calculateScore(s, numberOfPeople, seatingTypeId, seatingPreferenceId) == maxScore)
                .toList();

        return seatingMapper.toFilterResponseDto(mostMatchingSeatings);
    }

    /**
     * Returns IDs of seatings that are already booked
     * at the given date and time.
     */
    public List<Long> getBookedSeatings(LocalDateTime dateAndTime) {
        if (dateAndTime == null) {
            throw new IllegalArgumentException("Date and time can not be empty!");
        }

        return seatingRepository
                .findByReservations_StartTimeLessThanEqualAndReservations_EndTimeGreaterThanEqual(
                        dateAndTime,
                        dateAndTime
                )
                .stream()
                .map(Seating::getId)
                .toList();
    }

    /**
     * Calculates a score describing how well a seating matches
     * the requested criteria.
     */
    private double calculateScore(Seating seating, int numberOfPeople, Long seatingTypeId, Long seatingPreferenceId) {
        double score = 9.0 * (numberOfPeople / (double) seating.getMaxPeople());
        if (seatingTypeId != null && seatingTypeId.equals(seating.getSeatingType().getId())) {
            score += 3;
        }
        // If normal match then add 3 BUT if filtered by "Ratastooliga ligipääsetav" then that should be nr 1 priority.
        if (seatingPreferenceId != null && seating.getPreferences().stream().map(SeatingPreference::getId).toList().contains(seatingPreferenceId)) {
            score += seating.getPreferences().stream()
                    .filter(p -> p.getId().equals(seatingPreferenceId))
                    .findFirst()
                    .map(p -> "Ratastooliga ligipääsetav".equalsIgnoreCase(p.getName()) ? 100.0 : 3.0)
                    .orElse(0.0);
        }
        return score;
    }
}
