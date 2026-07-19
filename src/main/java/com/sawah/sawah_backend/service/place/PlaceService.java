package com.sawah.sawah_backend.service.place;

import com.sawah.sawah_backend.dto.place.*;
import com.sawah.sawah_backend.models.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface PlaceService {

    Place getPlaceById(Long id);

    PlaceWithDetailsResponseDto getPlaceWithDetailsById(Long placeId, Long userId);

    Page<PlaceWithoutDetailsResponseDto> toWithoutDetailsPage(Page<Place> places, Long userId);

    Page<Place> getAllPlaces(Pageable pageable);

    void addPlace(PlaceInputDto placeInputDto, List<MultipartFile> placeImages);

    void updatePlace(Long id, PlaceUpdateDto placeUpdateDto, List<MultipartFile> placeImages);

    void deletePlaceById(Long id);

    Page<Place> findByCategoryId(Long categoryId, Pageable pageable);

    Place findByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(String nameAr, String nameEn);

    Page<Place> findByGovernorateArContainingIgnoreCaseOrGovernorateEnContainingIgnoreCase(String govAr, String govEn,
            Pageable pageable);

    Page<Place> findFavoritePlacesByUserId(Long userId, Pageable pageable);

    Page<Place> findVisitedPlacesByUserId(Long userId, Pageable pageable);

    Page<Place> findAllByOrderByRatingDesc(Pageable pageable);

    Page<Place> findSuggestions(String query);

    Page<Place> getRecentSearches(Long userId);

    List<Place> findTop5NearbyByLocation(BigDecimal latitude, BigDecimal longitude);

    List<PlaceNearbyResponseDto> toNearbyResponseList(List<Place> places, Long userId);

    List<PlaceMapMarkerDto> getPlacesWithinBounds(MapBoundsDto bounds);

    List<PlaceWithoutDetailsResponseDto> getRecommendedPlaces(Long userId);

    Long placesCount();

}
