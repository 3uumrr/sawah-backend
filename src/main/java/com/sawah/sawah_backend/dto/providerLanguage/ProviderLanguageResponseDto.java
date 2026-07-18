package com.sawah.sawah_backend.dto.providerLanguage;

import com.sawah.sawah_backend.enums.LanguageLevel;

public record ProviderLanguageResponseDto(
        Long id,
        String nameAr,
        String nameEn,
        LanguageLevel proficiencyLevel
) {
}
