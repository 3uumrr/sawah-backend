package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.placePhoto.PlacePhotoResponseDto;
import com.sawah.sawah_backend.models.PlacePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PlacePhotoMapper {

    @Mapping(target = "url", source = "placePhoto", qualifiedByName = "toFullUrl")
    PlacePhotoResponseDto toDto(PlacePhoto placePhoto);

    List<PlacePhotoResponseDto> toDtoList(List<PlacePhoto> placePhotos);

    @Named("toFullUrl")
    default String toFullUrl(PlacePhoto placePhoto) {
        if (placePhoto == null || placePhoto.getUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/place_photos/")
                .path(placePhoto.getUrl())
                .toUriString();
    }
}
