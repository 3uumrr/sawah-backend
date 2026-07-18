package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.providerLanguage.ProviderLanguageResponseDto;
import com.sawah.sawah_backend.dto.providerLanguage.UpdateProviderLanguageDto;
import com.sawah.sawah_backend.models.ProviderLanguage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProviderLanguageMapper {

    @Mapping(target = "nameAr", source = "language.nameAr")
    @Mapping(target = "nameEn", source = "language.nameEn")
    ProviderLanguageResponseDto toDto(ProviderLanguage providerLanguage);

    List<ProviderLanguageResponseDto> toDtoList(List<ProviderLanguage> providerLanguages);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProviderLanguage toEntity(UpdateProviderLanguageDto dto);
}

