package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.language.LanguageInputDto;
import com.sawah.sawah_backend.models.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LanguageMapper {
    Language toEntity(LanguageInputDto languageDto);


    void updateEntityFromDto(LanguageInputDto dto, @MappingTarget Language entity);

}
