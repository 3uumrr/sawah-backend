package com.sawah.sawah_backend.dto.providerLanguage;

import com.sawah.sawah_backend.enums.LanguageLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProviderLanguageDto(
                Long id,
                @NotBlank(message = "{language.code.required}") String languageCode,

                @NotNull(message = "{language.level.required}") LanguageLevel proficiencyLevel) {
}
