package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.models.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

        Page<Place> findByCategoryId(Long categoryId, Pageable pageable);

        List<Place> findAllByIdOrderById(Long categoryId);

        Place findByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(String nameAr, String nameEn);

        Page<Place> findByGovernorateArContainingIgnoreCaseOrGovernorateEnContainingIgnoreCase(String govAr, String govEn, Pageable pageable);

        @Query(
                "SELECT p" +
                " FROM Place p" +
                " JOIN FavoritePlace fp" +
                " ON p.id = fp.place.id " +
                "WHERE fp.user.id = :userId" +
                " ORDER BY fp.createdAt DESC"
        )
        Page<Place> findFavoritePlacesByUserId(@Param("userId") Long userId, Pageable pageable);

        @Query(
                "SELECT p" +
                " FROM Place p " +
                "JOIN VisitedPlace vp" +
                " ON p.id = vp.place.id" +
                " WHERE vp.user.id = :userId" +
                " ORDER BY vp.visitedAt DESC"
        )
        Page<Place> findVisitedPlacesByUserId(@Param("userId") Long userId, Pageable pageable);

        // Most Popular
        Page<Place> findAllByOrderByRatingDesc(Pageable pageable);

        @Query("SELECT p " +
                "FROM Place p " +
                "WHERE LOWER(p.nameEn) LIKE LOWER(CONCAT(:query, '%')) " +
                "OR LOWER(p.nameAr) LIKE LOWER(CONCAT(:query, '%'))")
        Page<Place> findSuggestions(@Param("query") String query, Pageable pageable);


        @Query("SELECT p " +
                "FROM Place p" +
                " JOIN RecentSearch rs " +
                "ON p.id = rs.place.id " +
                " JOIN User u " +
                " ON u.id = rs.user.id " +
                " WHERE rs.user.id = :userId " +
                " ORDER BY rs.createdAt DESC ")
        Page<Place> getRecentSearches(@Param("userId") Long userId, Pageable pageable);


        @Query(value = "SELECT *, " +
                "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
                "cos(radians(p.longitude) - radians(:longitude)) + " +
                "sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
                "FROM places p " +
                "ORDER BY distance ASC " +
                "LIMIT 5",
                nativeQuery = true)
        List<Place> findTop5NearbyByLocation(
                @Param("latitude") BigDecimal latitude,
                @Param("longitude") BigDecimal longitude
        );

        @Query("SELECT new com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto(" +
                "p.id, p.nameAr, p.nameEn, p.longitude, p.latitude) " +
                "FROM Place p " +
                "WHERE p.latitude BETWEEN :minLat AND :maxLat " +
                "AND p.longitude BETWEEN :minLng AND :maxLng")
        List<com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto> findPlacesWithinBounds(
                @Param("minLat") BigDecimal minLat,
                @Param("maxLat") BigDecimal maxLat,
                @Param("minLng") BigDecimal minLng,
                @Param("maxLng") BigDecimal maxLng
        );


}
