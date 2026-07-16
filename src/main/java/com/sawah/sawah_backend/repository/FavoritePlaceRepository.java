package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<FavoritePlace> findByUserIdAndPlaceId(Long userId, Long placeId);

    @Query("SELECT fp.place.id FROM FavoritePlace fp WHERE fp.user.id = :userId AND fp.place.id IN :placeIds")
    Set<Long> findPlaceIdsByUserIdAndPlaceIdIn(@Param("userId") Long userId, @Param("placeIds") Collection<Long> placeIds);
}
