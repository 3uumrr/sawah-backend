package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.placePrice.PlacePriceResponseDto;
import com.sawah.sawah_backend.models.PlacePrice;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PlacePriceMapper {

    PlacePriceResponseDto toDto(PlacePrice placePrice);

    List<PlacePriceResponseDto> toDtoList(List<PlacePrice> placePrices);
}
