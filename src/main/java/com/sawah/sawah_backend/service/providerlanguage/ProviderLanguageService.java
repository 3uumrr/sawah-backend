package com.sawah.sawah_backend.service.providerlanguage;

import com.sawah.sawah_backend.enums.LanguageLevel;
import com.sawah.sawah_backend.models.ProviderLanguage;

import java.util.List;

public interface ProviderLanguageService {
    ProviderLanguage getById(Long id);
    List<ProviderLanguage> getAll();
    List<ProviderLanguage> getByProviderId(Long providerId);
    void create(LanguageLevel proficiencyLevel , Long providerId , Long languageId );
    void update(Long id, String proficiencyLevel );
    void delete(Long id);

    void flushChanges();
}
