package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.RecentSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

    Optional<RecentSearch> findByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<RecentSearch> findByIdAndUserId(Long id, Long userId);

    void deleteByUserId(Long userId);
}
