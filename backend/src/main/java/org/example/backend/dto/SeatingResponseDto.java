package org.example.backend.dto;

import lombok.Data;

@Data
public class SeatingResponseDto {
    private Long id;
    private String name;
    private Long typeId;
    private int maxPeople;
}
