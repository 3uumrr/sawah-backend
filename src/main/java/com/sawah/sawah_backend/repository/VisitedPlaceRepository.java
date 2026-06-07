package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<VisitedPlace> findByUserIdAndPlaceId(Long userId, Long placeId);

    @Query("SELECT vp.place.id FROM VisitedPlace vp WHERE vp.user.id = :userId AND vp.place.id IN :placeIds")
    Set<Long> findPlaceIdsByUserIdAndPlaceIdIn(@Param("userId") Long userId, @Param("placeIds") Collection<Long> placeIds);
}
