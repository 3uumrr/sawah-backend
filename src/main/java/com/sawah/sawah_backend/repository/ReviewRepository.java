package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.place.id = :placeId")
    Page<Review> findByPlaceIdOrderByCreatedAtDesc(Long placeId, Pageable pageable);
    Page<Review> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<Review> findByUserIdAndPlaceId(Long userId, Long placeId);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    long countByPlaceId(Long placeId);

    int countByPlaceIdAndStars(Long placeId, Integer stars);

    @Query("SELECT AVG(r.stars) FROM Review r WHERE r.place.id = :placeId")
    Double findAverageStarsByPlaceId(@Param("placeId") Long placeId);

    void deleteByUserId(Long userId);
}
