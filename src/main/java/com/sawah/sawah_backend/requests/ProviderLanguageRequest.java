package com.sawah.sawah_backend.requests;

import com.sawah.sawah_backend.enums.LanguageLevel;

public record ProviderLanguageRequest(
        String languageCode,
        LanguageLevel languageLevel
) {
}
