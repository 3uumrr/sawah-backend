package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.place.PlaceNearbyResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceRecentSearchResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceWithDetailsResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceWithoutDetailsResponseDto;
import com.sawah.sawah_backend.dto.recommendation.PlaceFeatureDto;
import com.sawah.sawah_backend.models.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring", uses = { PlacePhotoMapper.class, PlacePriceMapper.class,
        ReviewMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PlaceMapper {

    @Mapping(target = "averageRating", source = "rating")
    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toFullUrl")
    @Mapping(target = "isFavorite", expression = "java(isFavorite(place, favoritePlaceIds))")
    PlaceWithoutDetailsResponseDto toWithoutDetailsDto(Place place, @Context Set<Long> favoritePlaceIds);

    @Mapping(target = "averageRating", source = "rating")
    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toFullUrl")
    @Mapping(target = "isFavorite", expression = "java(isFavorite(place, favoritePlaceIds))")
    PlaceNearbyResponseDto toNearbyResponseDto(Place place, @Context Set<Long> favoritePlaceIds);

    List<PlaceWithoutDetailsResponseDto> toPlaceWithoutDetailsResponseDto(
            List<Place> places, @Context Set<Long> favoritePlaceIds);

    @Mapping(target = "id", source = "place.id")
    @Mapping(target = "nameEn", source = "place.nameEn")
    @Mapping(target = "nameAr", source = "place.nameAr")
    @Mapping(target = "governorateAr", source = "place.governorateAr")
    @Mapping(target = "governorateEn", source = "place.governorateEn")
    @Mapping(target = "totalReviews", source = "place.totalReviews")
    @Mapping(target = "descriptionAr", source = "place.descriptionAr")
    @Mapping(target = "descriptionEn", source = "place.descriptionEn")
    @Mapping(target = "openTime", source = "place.openTime")
    @Mapping(target = "closeTime", source = "place.closeTime")
    @Mapping(target = "longitude", source = "place.longitude")
    @Mapping(target = "latitude", source = "place.latitude")
    @Mapping(target = "bookingUrl", source = "place.bookingUrl")
    @Mapping(target = "averageRating", source = "place.rating")
    @Mapping(target = "photos", source = "placePhotos")
    @Mapping(target = "prices", source = "placePrices")
    @Mapping(target = "reviews", source = "reviews")
    @Mapping(target = "numOfStars", source = "numOfStars")
    @Mapping(target = "isFavorite", expression = "java(isFavorite(place, context.getFavoritePlaceIds()))")
    @Mapping(target = "isVisited", expression = "java(isVisited(place, context.getVisitedPlaceIds()))")
    PlaceWithDetailsResponseDto toWithDetailsDto(
            Place place,
            List<PlacePhoto> placePhotos,
            List<PlacePrice> placePrices,
            List<Review> reviews,
            Map<String, Integer> numOfStars,
            @Context PlaceDetailsContext context,
            @Context Long currentUserId
    );

    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toFullUrl")
    PlaceRecentSearchResponseDto toRecentSearchResponseDto(Place place);

    @Mapping(target = "averageRating", source = "rating")
    @Mapping(target = "category", source = "category.nameEn")
    PlaceFeatureDto toPlaceFeatureDto(Place place);

    @Mapping(target = "averageRating", source = "rating")
    List<PlaceFeatureDto> toListPlaceFeatureDto(List<Place> place);

    @Mapping(target = "mainImageUrl", source = "place", qualifiedByName = "toFullUrl")
    List<PlaceRecentSearchResponseDto> toRecentSearchResponseDto(List<Place> places);
    default boolean isFavorite(Place place, Set<Long> favoritePlaceIds) {
        if (place == null || place.getId() == null || favoritePlaceIds == null) {
            return false;
        }
        return favoritePlaceIds.contains(place.getId());
    }

    default boolean isVisited(Place place, Set<Long> visitedPlaceIds) {
        if (place == null || place.getId() == null || visitedPlaceIds == null) {
            return false;
        }
        return visitedPlaceIds.contains(place.getId());
    }

    @Named("toFullUrl")
    default String toFullUrl(Place place) {
        if (place == null || place.getMainImageUrl() == null)
            return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/place_photos/")
                .path(place.getMainImageUrl())
                .toUriString();
    }

    default Map<String, Integer> numOfStars(List<Review> reviews) {
        // ماب جاهزة بالمفاتيح وقيمة ابتدائية صفر
        Map<String, Integer> stars = new HashMap<>(Map.of("one", 0, "two", 0, "three", 0, "four", 0, "five", 0));

        if (reviews == null)
            return stars;

        String[] starNames = { "one", "two", "three", "four", "five" };

        reviews.forEach(review -> {
            stars.merge(starNames[review.getStars() - 1], 1, Integer::sum);
        });

        return stars;
    }

}
