package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.mapper.SeatingMapper;
import org.example.backend.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingService {

    private final SeatingRepository seatingRepository;
    private final SeatingMapper seatingMapper;

    public List<SeatingResponseDto> getAllSeating() {
        return seatingMapper.toResponseDto(seatingRepository.findAll());
    }
}
