package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.ProviderReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderReviewRepository extends JpaRepository<ProviderReview, Long> {

    @Query("SELECT pr FROM ProviderReview pr " +
            "JOIN FETCH pr.provider p " +
            "JOIN FETCH p.user u " +
            "WHERE p.id = :providerId")
    Page<ProviderReview> findAllByProviderId(@Param("providerId") Long providerId, Pageable pageable);

    boolean existsByServiceRequest_Id(Long serviceRequestId);

    long countByProvider_Id(Long providerId);

    @Query("SELECT AVG(pr.stars) FROM ProviderReview pr WHERE pr.provider.id = :providerId")
    Double findAverageStarsByProviderId(@Param("providerId") Long providerId);

    void deleteByTouristId(Long touristId);

}
