package com.sawah.sawah_backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record CategoryInputDto(

        @NotBlank(message = "category.nameAr.required")
        @Size(min = 4, max = 50, message = "category.nameAr.size")
        String nameAr,

        @NotBlank(message = "category.nameEn.required")
        @Size(min = 4, max = 50, message = "category.nameEn.size")
        String nameEn,

        @NotNull(message = "category.displayOrder.required")
        Integer displayOrder
)
{ }
