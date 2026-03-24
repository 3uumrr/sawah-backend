package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.LanguageInputDto;
import com.sawah.sawah_backend.dto.ServiceInputDto;
import com.sawah.sawah_backend.models.Language;
import com.sawah.sawah_backend.models.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LanguageMapper {
    Language toEntity(LanguageInputDto languageDto);

    LanguageInputDto toLanguageDto(Language language);

    void updateEntityFromDto(LanguageInputDto dto, @MappingTarget Language entity);

}
