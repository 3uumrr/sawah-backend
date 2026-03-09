package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.LanguageInputDto;
import com.sawah.sawah_backend.models.Language;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LanguageMapper {
    Language toEntity(LanguageInputDto languageDto);

    LanguageInputDto toLanguageDto(Language language);

    List<LanguageInputDto> toListLanguageDto(List<Language> languages);

}
