package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.models.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {


    @Query("SELECT p FROM Provider p JOIN FETCH p.user JOIN FETCH p.service WHERE p.user.id = :userId")
    Optional<Provider> findByUserId(@Param("userId") Long userId);
    boolean existsByUserId(Long userId);

    boolean existsByNationalId(String nationalId);

    @Query("SELECT p FROM Provider p " +
            "JOIN p.service s " +
            "WHERE s.code = :serviceCode " +
            "AND p.accountStatus = :status " +
            "AND (:isAvailable IS NULL OR p.isAvailable = :isAvailable)")
    Page<Provider> findByServiceCodeAndStatus(
            @Param("serviceCode") ServiceCode serviceCode,
            @Param("status") ProviderStatus status,
            @Param("isAvailable") Boolean isAvailable,
            Pageable pageable);

    @Query(
            value = "SELECT p FROM Provider p " +
                    "JOIN FETCH p.user " +
                    "JOIN FETCH p.service s " +
                    "WHERE (:status IS NULL OR p.accountStatus = :status) " +
                    "AND (:serviceCode IS NULL OR s.code = :serviceCode) " +
                    "AND (:isAvailable IS NULL OR p.isAvailable = :isAvailable)"
    )
    Page<Provider> findProvidersForAdmin(
            @Param("status") ProviderStatus status,
            @Param("serviceCode") ServiceCode serviceCode,
            @Param("isAvailable") Boolean isAvailable,
            Pageable pageable);

    @Query("SELECT p FROM Provider p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.service " +
            "WHERE p.id = :providerId")
    Optional<Provider> findByIdWithUserAndService(@Param("providerId") Long providerId);


    @Query("SELECT p.accountStatus FROM Provider p WHERE p.user.id = :userId")
    Optional<ProviderStatus> getProviderStatusByUserId(@Param("userId") Long userId);


    @Query("SELECT COUNT(p) " +
            " FROM Provider p " +
            " WHERE p.accountStatus = :providerStatus")
    Long countAllProvidersByProviderStatus(@Param("providerStatus") ProviderStatus providerStatus);
}
