package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.ProviderLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderLanguageRepository extends JpaRepository<ProviderLanguage, Long> {
    List<ProviderLanguage> findByProviderId(Long providerId);

    void deleteByProviderId(Long providerId);


}
