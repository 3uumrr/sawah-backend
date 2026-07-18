package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.service.ServiceInputDto;
import com.sawah.sawah_backend.models.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServiceMapper {
    Service toEntity(ServiceInputDto serviceInputDto);

    ServiceInputDto toServiceDto(Service service);

    void updateEntityFromDto(ServiceInputDto dto, @MappingTarget Service entity);

}
