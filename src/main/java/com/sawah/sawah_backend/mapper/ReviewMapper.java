package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.review.ReviewResponseDto;
import com.sawah.sawah_backend.models.Review;
import com.sawah.sawah_backend.models.User;
import org.mapstruct.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReviewMapper {

    @Mapping(target = "comment", source = "content")
    @Mapping(target = "touristName", expression = "java(toTouristName(review))")
    @Mapping(target = "isOwnReview", expression = "java(isOwnReview(review, currentUserId))")
    @Mapping(target = "touristImage", source = "review.user", qualifiedByName = "toTouristImage")
    ReviewResponseDto toDto(Review review, @Context Long currentUserId);

    List<ReviewResponseDto> toDtoList(List<Review> reviews, @Context Long currentUserId);

    default String toTouristName(Review review) {
        if (review == null || review.getUser() == null) return null;

        User user = review.getUser();
        String firstName = user.getFirstName() == null ? "" : user.getFirstName();
        String lastName = user.getLastName() == null ? "" : user.getLastName();
        String fullName = (firstName + " " + lastName).trim();

        return fullName.isEmpty() ? null : fullName;
    }

    @Named("toTouristImage")
    default String toTouristImage(User user) {
        if (user.getProfilePictureUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(user.getProfilePictureUrl())
                .toUriString();
    }

    default Boolean isOwnReview(Review review, @Context Long currentUserId) {
        if (review == null || review.getUser() == null || currentUserId == null) return false;
        return Objects.equals(review.getUser().getId(), currentUserId);
    }

}
