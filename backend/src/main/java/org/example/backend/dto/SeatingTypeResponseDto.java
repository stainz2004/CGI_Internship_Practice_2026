package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatingTypeResponseDto {
    private Long id;
    private String type;
}
