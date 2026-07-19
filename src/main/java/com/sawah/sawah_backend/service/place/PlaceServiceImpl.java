package com.sawah.sawah_backend.service.place;

import com.sawah.sawah_backend.dto.place.*;
import com.sawah.sawah_backend.dto.recommendation.PlaceFeatureDto;
import com.sawah.sawah_backend.dto.recommendation.RecommendationRequestDto;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.PlaceMapper;
import com.sawah.sawah_backend.mapper.PlaceDetailsContext;
import com.sawah.sawah_backend.models.*;
import com.sawah.sawah_backend.repository.PlaceRepository;
import com.sawah.sawah_backend.service.category.CategoryService;
import com.sawah.sawah_backend.service.favoritePlace.FavoritePlaceService;
import com.sawah.sawah_backend.service.placePhoto.PlacePhotoService;
import com.sawah.sawah_backend.service.placePrice.PlacePriceService;
import com.sawah.sawah_backend.service.review.ReviewService;
import com.sawah.sawah_backend.service.user.UserService;
import com.sawah.sawah_backend.service.userPreference.UserPreferencesService;
import com.sawah.sawah_backend.service.visitedPlace.VisitedPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{

    private final PlaceRepository placeRepository;
    private final PlacePriceService  placePriceService;
    private final CategoryService categoryService;
    private final PlacePhotoService  placePhotoService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final FavoritePlaceService favoritePlaceService;
    private final VisitedPlaceService  visitedPlaceService;
    private final PlaceMapper placeMapper;
    private final UserPreferencesService  userPreferencesService;
    private final com.sawah.sawah_backend.service.recommendation.RecommendationService recommendationService;


    @Override
    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("place.not.found"));
    }

    @Override
    public PlaceWithDetailsResponseDto getPlaceWithDetailsById(Long placeId , Long userId) {
        Place place = getPlaceById(placeId);

        List<PlacePrice> placePrices = placePriceService.getByPlaceId(placeId);
        List<PlacePhoto> placePhotos = placePhotoService.getByPlaceId(placeId);

        Page<Review> reviews = reviewService.getByPlaceId(placeId, PageRequest.of(0, 5));
        Map<String, Integer> numOfStars = reviewService.getStarDistributionByPlaceId(placeId);

        Set<Long> favoriteIds = favoritePlaceService.findFavoritePlaceIds(userId, List.of(place.getId()));
        Set<Long> visitedIds = visitedPlaceService.findVisitedPlaceIds(userId, List.of(place.getId()));


        return placeMapper.toWithDetailsDto(place, placePhotos, placePrices, reviews.getContent(), numOfStars, new PlaceDetailsContext(favoriteIds, visitedIds), userId);
    }

    @Override
    public Page<PlaceWithoutDetailsResponseDto> toWithoutDetailsPage(Page<Place> places, Long userId) {
        List<Long> placeIds = places.getContent().stream()
                .map(Place::getId)
                .collect(Collectors.toList());
        Set<Long> favoriteIds = favoritePlaceService.findFavoritePlaceIds(userId, placeIds);
        return places.map(place -> placeMapper.toWithoutDetailsDto(place, favoriteIds));
    }

    @Override
    public Page<Place> getAllPlaces(Pageable pageable) {
        return placeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void addPlace(PlaceInputDto placeInputDto ,  List<MultipartFile> placeImages) {
        // 1. Get Category By Id
        Category category = categoryService.getCategoryById(placeInputDto.categoryId());
        // 2. Save Place
        Place place = Place.builder()
                .nameAr(placeInputDto.nameAr())
                .nameEn(placeInputDto.nameEn())
                .governorateAr(placeInputDto.governorateAr())
                .governorateEn(placeInputDto.governorateEn())
                .descriptionAr(placeInputDto.descriptionAr())
                .descriptionEn(placeInputDto.descriptionEn())
                .openTime(placeInputDto.openTime())
                .closeTime(placeInputDto.closeTime())
                .latitude(placeInputDto.latitude())
                .longitude(placeInputDto.longitude())
                .bookingUrl(placeInputDto.bookingUrl())
                .category(category)
                .build();

        // 3. Save Place
        place = placeRepository.save(place);

        final Place finalPlace = place;
        // 4. Save Place Prices If Exists
        if (placeInputDto.prices() != null) {
            placeInputDto.prices().forEach(p -> {
                placePriceService.create(
                        p.visitorCategoryEn(),
                        p.visitorNationalityEn(),
                        p.visitorCategoryAr(),
                        p.visitorNationalityAr(),
                        p.price(),
                        finalPlace.getId()
                );
            });
        }

        // 5. Save Place Images
        if (placeImages == null || placeImages.isEmpty()) {
            throw new BadRequestException("place.images.required");
        }

        for (int i = 0; i < placeImages.size(); i++) {
            if (i == 0){
                place.setMainImageUrl(placePhotoService.create(placeImages.get(i), i + 1, finalPlace.getId()));
            } else {
                placePhotoService.create(placeImages.get(i), i + 1, finalPlace.getId());
            }
        }

        placeRepository.save(place);
    }

    @Override
    @Transactional
    public void updatePlace(Long id, PlaceUpdateDto placeUpdateDto, List<MultipartFile> placeImages) {
        Place place = getPlaceById(id);
        Category category = categoryService.getCategoryById(placeUpdateDto.categoryId());

        place.setNameAr(placeUpdateDto.nameAr());
        place.setNameEn(placeUpdateDto.nameEn());
        place.setGovernorateAr(placeUpdateDto.governorateAr());
        place.setGovernorateEn(placeUpdateDto.governorateEn());
        place.setDescriptionAr(placeUpdateDto.descriptionAr());
        place.setDescriptionEn(placeUpdateDto.descriptionEn());
        place.setOpenTime(placeUpdateDto.openTime());
        place.setCloseTime(placeUpdateDto.closeTime());
        place.setLatitude(placeUpdateDto.latitude());
        place.setLongitude(placeUpdateDto.longitude());
        place.setBookingUrl(placeUpdateDto.bookingUrl());
        place.setCategory(category);

        placePriceService.getByPlaceId(place.getId())
                .forEach(placePrice -> placePriceService.delete(placePrice.getId()));

        if (placeUpdateDto.prices() != null) {
            placeUpdateDto.prices().forEach(p -> placePriceService.create(
                    p.visitorCategoryEn(),
                    p.visitorNationalityEn(),
                    p.visitorCategoryAr(),
                    p.visitorNationalityAr(),
                    p.price(),
                    place.getId()
            ));
        }

        if (placeImages != null && !placeImages.isEmpty()) {
            placePhotoService.deleteAllByPlaceId(place.getId());

            for (int i = 0; i < placeImages.size(); i++) {
                String imageUrl = placePhotoService.create(placeImages.get(i), i + 1, place.getId());
                if (i == 0) {
                    place.setMainImageUrl(imageUrl);
                }
            }
        }

        placeRepository.save(place);
    }

    @Override
    @Transactional
    public void deletePlaceById(Long id) {
        Place place = getPlaceById(id);
        placePhotoService.deleteAllByPlaceId(place.getId());
        placeRepository.delete(place);
    }

    @Override
    public Page<Place> findByCategoryId(Long categoryId, Pageable pageable) {
        categoryService.getCategoryById(categoryId);
        return placeRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Place findByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(String nameAr, String nameEn) {
        Place place = placeRepository.findByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(nameAr, nameEn);
        if (place == null) {
            throw new ResourceNotFoundException("place.not.found");
        }
        return place;
    }

    @Override
    public Page<Place> findByGovernorateArContainingIgnoreCaseOrGovernorateEnContainingIgnoreCase(String govAr, String govEn, Pageable pageable) {
        return placeRepository.findByGovernorateArContainingIgnoreCaseOrGovernorateEnContainingIgnoreCase(govAr, govEn, pageable);
    }

    @Override
    public Page<Place> findFavoritePlacesByUserId(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        return placeRepository.findFavoritePlacesByUserId(user.getId(), pageable);
    }

    @Override
    public Page<Place> findVisitedPlacesByUserId(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        return placeRepository.findVisitedPlacesByUserId(user.getId(), pageable);
    }

    @Override
    public Page<Place> findAllByOrderByRatingDesc(Pageable pageable) {
        return placeRepository.findAllByOrderByRatingDesc(pageable);
    }

    @Override
    public Page<Place> findSuggestions(String query) {
        Pageable page = PageRequest.of(0, 7);
        return placeRepository.findSuggestions(query, page);
    }

    @Override
    public Page<Place> getRecentSearches(Long userId) {
        Pageable page = PageRequest.of(0, 7);
        return placeRepository.getRecentSearches(userId, page);
    }

    @Override
    public List<Place> findTop5NearbyByLocation(BigDecimal latitude, BigDecimal longitude) {
        return placeRepository.findTop5NearbyByLocation(latitude, longitude);
    }

    @Override
    public List<PlaceNearbyResponseDto> toNearbyResponseList(List<Place> places, Long userId) {
        List<Long> placeIds = places.stream()
                .map(Place::getId)
                .collect(Collectors.toList());
        Set<Long> favoriteIds = favoritePlaceService.findFavoritePlaceIds(userId, placeIds);
        return places.stream()
                .map(place -> placeMapper.toNearbyResponseDto(place, favoriteIds))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceMapMarkerDto> getPlacesWithinBounds(MapBoundsDto bounds) {
        return placeRepository.findPlacesWithinBounds(
                bounds.minLat(), bounds.maxLat(),
                bounds.minLng(), bounds.maxLng()
        );
    }

    @Override
    public List<PlaceWithoutDetailsResponseDto> getRecommendedPlaces(Long userId) {
        List<PlaceFeatureDto> allPlaces = placeRepository.findAll().stream()
                .map(p -> new PlaceFeatureDto(
                        p.getId(), p.getCategory().getNameEn(), p.getRating()
                )).toList();

        List<PlaceFeatureDto> visitedPlaces = placeMapper
                .toListPlaceFeatureDto(placeRepository.findVisitedPlacesByUserId(userId, Pageable.unpaged()).toList());


        List<PlaceFeatureDto> favoritePlaces = placeMapper
                .toListPlaceFeatureDto(placeRepository.findFavoritePlacesByUserId(userId, Pageable.unpaged()).toList());


        List<String> preferredCategories = userPreferencesService.getUserCategories(userId).stream()
                .map(Category::getNameEn).collect(Collectors.toList());

        for (String preferredCategory : preferredCategories) {
            System.out.println(preferredCategory);
        }

        RecommendationRequestDto request =
                new RecommendationRequestDto(
                        allPlaces, visitedPlaces, favoritePlaces, preferredCategories
                );

        List<Long> recommendedIds = recommendationService.getRecommendations(request);

        if (recommendedIds == null || recommendedIds.isEmpty()) {
            return List.of();
        }

        List<Place> unorderedPlaces = placeRepository.findAllById(recommendedIds);

        List<Place> recommendedPlaces = unorderedPlaces.stream()
                .sorted(Comparator.comparingInt(place -> recommendedIds.indexOf(place.getId())))
                .collect(Collectors.toList());

        Set<Long> favoriteIds = favoritePlaceService.findFavoritePlaceIds(userId, recommendedIds);
        
        return placeMapper.toPlaceWithoutDetailsResponseDto(recommendedPlaces, favoriteIds);
    }

    @Override
    public Long placesCount() {
        return placeRepository.count();
    }
}
