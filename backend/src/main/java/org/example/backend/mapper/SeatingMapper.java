package org.example.backend.mapper;

import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.entity.Seating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatingMapper {

    @Mapping(source = "seatingType.id", target = "typeId")
    SeatingResponseDto toResponseDto(Seating seating);

    List<SeatingResponseDto> toResponseDto(List<Seating> seating);
}
