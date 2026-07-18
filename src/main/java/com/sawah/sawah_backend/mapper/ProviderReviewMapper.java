package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.providerReview.ProviderReviewResponseDto;
import com.sawah.sawah_backend.models.ProviderReview;
import com.sawah.sawah_backend.models.User;
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
public interface ProviderReviewMapper {

    @Mapping(target = "touristName", expression = "java(toTouristName(providerReview))")
    @Mapping(target = "touristImage", source = "tourist", qualifiedByName = "toTouristImage")
    ProviderReviewResponseDto toDto(ProviderReview providerReview);

    List<ProviderReviewResponseDto> toDtoList(List<ProviderReview> providerReviews);

    default String toTouristName(ProviderReview providerReview) {
        if (providerReview == null || providerReview.getTourist() == null) return null;

        User tourist = providerReview.getTourist();
        String firstName = tourist.getFirstName() == null ? "" : tourist.getFirstName();
        String lastName = tourist.getLastName() == null ? "" : tourist.getLastName();
        String fullName = (firstName + " " + lastName).trim();

        return fullName.isEmpty() ? null : fullName;
    }

    @Named("toTouristImage")
    default String toTouristImage(User tourist) {
        if (tourist == null || tourist.getProfilePictureUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(tourist.getProfilePictureUrl())
                .toUriString();
    }
}
