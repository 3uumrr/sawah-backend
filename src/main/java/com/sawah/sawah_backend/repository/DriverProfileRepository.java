package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.VehicleType;
import com.sawah.sawah_backend.models.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    Optional<DriverProfile> findByProviderId(Long providerId);

    boolean existsByProviderId(Long providerId);

    void deleteByProviderId(Long providerId);

}
