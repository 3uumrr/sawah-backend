package com.sawah.sawah_backend.service.providerlanguage;

import com.sawah.sawah_backend.enums.LanguageLevel;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Language;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.ProviderLanguage;
import com.sawah.sawah_backend.repository.LanguageRepository;
import com.sawah.sawah_backend.repository.ProviderLanguageRepository;
import com.sawah.sawah_backend.repository.ProviderRepository;
import com.sawah.sawah_backend.service.language.LanguageService;
import com.sawah.sawah_backend.service.provider.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderLanguageServiceImpl implements ProviderLanguageService {

    private final ProviderLanguageRepository providerLanguageRepository;
    private final ProviderRepository providerRepository;
    private final LanguageService languageService;

    @Override
    public ProviderLanguage getById(Long id) {
        return providerLanguageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("provider.language.not.found"));
    }

    @Override
    public List<ProviderLanguage> getAll() {
        return providerLanguageRepository.findAll();
    }

    @Override
    public List<ProviderLanguage> getByProviderId(Long providerId) {
        return providerLanguageRepository.findByProviderId(providerId);
    }

    @Override
    @Transactional
    public void create(LanguageLevel proficiencyLevel  , Long providerId , Long languageId) {

        Language language = languageService.getById(languageId);

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("provider.not.found"));

        ProviderLanguage providerLanguage = ProviderLanguage.builder()
                .proficiencyLevel(proficiencyLevel)
                .language(language)
                .provider(provider)
                .build();

         providerLanguageRepository.save(providerLanguage);
    }

    @Override
    public void update(Long id, String proficiencyLevel) {
        ProviderLanguage providerLanguage = getById(id);

        providerLanguage.setProficiencyLevel(Enum.valueOf(LanguageLevel.class,proficiencyLevel));

        providerLanguageRepository.save(providerLanguage);
    }

    @Override
    public void delete(Long id) {
        providerLanguageRepository.deleteById(id);
    }

    @Override
    public void flushChanges() {
        providerLanguageRepository.flush();
    }


}
