package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByCode(ServiceCode code);
}

