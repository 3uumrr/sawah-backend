package com.sawah.sawah_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LanguageInputDto(
        @NotNull
        @Size(min = 3, max = 50)
        String nameAr,

        @NotNull
        @Size(min = 3, max = 50)
        String nameEn,

        @NotNull
        @Size(min = 2, max = 10)
        String code
) {
}
