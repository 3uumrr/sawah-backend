package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.models.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    void deleteByTouristId(Long touristId);

    void deleteByProviderId(Long providerId);

    @Query("SELECT sr from ServiceRequest sr" +
            " JOIN FETCH sr.place" +
            " JOIN FETCH sr.provider" +
            " JOIN FETCH sr.tourist t " +
            " WHERE sr.provider.id = :providerId" +
            " ORDER BY sr.createdAt desc ")
    Page<ServiceRequest> findByProviderId(@Param("providerId") Long providerId, Pageable pageable);

    @Query("SELECT sr from ServiceRequest sr" +
            " JOIN FETCH sr.place" +
            " JOIN FETCH sr.provider" +
            " JOIN FETCH sr.tourist t " +
            " WHERE sr.provider.id = :providerId AND sr.status = :status" +
            " ORDER BY sr.createdAt desc ")
    Page<ServiceRequest> findByProviderIdAndStatus(@Param("providerId") Long providerId, @Param("status") ServiceRequestStatus status, Pageable pageable);

    @Query(
            value = "SELECT sr from ServiceRequest sr" +
                    " JOIN FETCH sr.place" +
                    " JOIN FETCH sr.provider p" +
                    " JOIN FETCH p.user" +
                    " JOIN FETCH sr.service" +
                    " JOIN FETCH sr.tourist" +
                    " WHERE sr.tourist.id = :touristId" +
                    " ORDER BY sr.createdAt desc ",
            countQuery = "SELECT COUNT(sr) from ServiceRequest sr" +
                    " WHERE sr.tourist.id = :touristId")
    Page<ServiceRequest> findByTouristId(@Param("touristId") Long touristId, Pageable pageable);

    @Query(
            value = "SELECT sr from ServiceRequest sr" +
                    " JOIN FETCH sr.place" +
                    " JOIN FETCH sr.provider p" +
                    " JOIN FETCH p.user" +
                    " JOIN FETCH sr.service" +
                    " JOIN FETCH sr.tourist" +
                    " WHERE sr.tourist.id = :touristId AND sr.status = :status" +
                    " ORDER BY sr.createdAt desc ",
            countQuery = "SELECT COUNT(sr) from ServiceRequest sr" +
                    " WHERE sr.tourist.id = :touristId AND sr.status = :status")
    Page<ServiceRequest> findByTouristIdAndStatus(@Param("touristId") Long touristId, @Param("status") ServiceRequestStatus status, Pageable pageable);


    @Query("SELECT COALESCE(SUM(sr.totalPrice), 0.0) FROM ServiceRequest sr " +
            "WHERE sr.provider.id = :providerId " +
            "AND sr.status = 'COMPLETED' " +
            "AND sr.completedAt >= :startDate")
    Double sumEarningsSince(@Param("providerId") Long providerId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(sr.totalPrice), 0.0) FROM ServiceRequest sr " +
            "WHERE sr.provider.id = :providerId " +
            "AND sr.status = 'COMPLETED'")
    Double sumTotalEarnings(@Param("providerId") Long providerId);


    @Query("SELECT COUNT(sr) " +
            "FROM ServiceRequest sr" +
            " WHERE sr.provider.id = :providerId" +
            " AND sr.status = :status")
    Long countByProviderAndStatus(@Param("providerId") Long providerId, @Param("status") ServiceRequestStatus status);






}
