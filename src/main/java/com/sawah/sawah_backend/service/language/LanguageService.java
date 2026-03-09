package com.sawah.sawah_backend.service.language;

import com.sawah.sawah_backend.dto.LanguageInputDto;
import com.sawah.sawah_backend.models.Language;

import java.util.List;

public interface LanguageService {
    Language getByCode(String code);
    Language getById(Long id);
    List<Language> getLanguages();

    // For Admin
    void deleteById(Long id);
    void addLanguage(LanguageInputDto language);
    void updateLanguage(LanguageInputDto language , Long id);

    void initLanguages();
}
