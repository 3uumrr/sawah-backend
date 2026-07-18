package com.sawah.sawah_backend.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceInputDto (

        @NotBlank(message = "service.nameAr.required")
        @Size(min = 5, max = 50, message = "service.nameAr.size")
        String nameAr,

        @NotBlank(message = "service.nameEn.required")
        @Size(min = 5, max = 50, message = "service.nameEn.size")
        String nameEn,

        @NotBlank(message = "service.code.required")
        @Size(min = 5, max = 50, message = "service.code.size")
        String code

) {}
