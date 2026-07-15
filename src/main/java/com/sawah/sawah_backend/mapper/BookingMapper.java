package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.booking.BookingRequestDto;
import com.sawah.sawah_backend.dto.booking.ProviderBookingResponseDto;
import com.sawah.sawah_backend.dto.booking.TouristBookingResponseDto;
import com.sawah.sawah_backend.models.ServiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "bookingDateTime", expression = "java(java.time.LocalDateTime.of(dto.bookingDate(), dto.bookingTime()))")
    @Mapping(target = "place", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "tourist", ignore = true)
    ServiceRequest toEntity(BookingRequestDto dto);

    @Mapping(source = "id", target = "bookingId")
    @Mapping(source = "place.nameEn", target = "placeNameEn")
    @Mapping(source = "place.nameAr", target = "placeNameAr")
    @Mapping(target = "placeImageUrl", source = "serviceRequest", qualifiedByName = "toFullUrl")
    @Mapping(source = "place.governorateAr", target = "governorateAr")
    @Mapping(source = "place.governorateEn", target = "governorateEn")
    @Mapping(target = "touristFullName", expression = "java(serviceRequest.getTourist().getFirstName() + \" \" + serviceRequest.getTourist().getLastName())")
    @Mapping(source = "tourist.phoneNumber", target = "touristPhoneNumber")
    @Mapping(source = "service.nameEn", target = "serviceNameEn")
    @Mapping(source = "service.nameAr", target = "serviceNameAr")
    @Mapping(source = "additionalNotes", target = "additionalNotes")
    @Mapping(target = "bookingDate", expression = "java(serviceRequest.getBookingDateTime().toLocalDate())")
    @Mapping(target = "bookingTime", expression = "java(serviceRequest.getBookingDateTime().toLocalTime())")
    ProviderBookingResponseDto toProviderBookingResponseDto(ServiceRequest serviceRequest);

    @Mapping(source = "id", target = "bookingId")
    @Mapping(source = "place.nameEn", target = "placeNameEn")
    @Mapping(source = "place.nameAr", target = "placeNameAr")
    @Mapping(target = "placeImageUrl", source = "serviceRequest", qualifiedByName = "toFullUrl")
    @Mapping(source = "place.governorateEn", target = "governorateEn")
    @Mapping(source = "place.governorateAr", target = "governorateAr")
    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(target = "providerFullName", expression = "java(serviceRequest.getProvider().getUser().getFirstName() + \" \" + serviceRequest.getProvider().getUser().getLastName())")
    @Mapping(source = "provider.user.phoneNumber", target = "providerPhoneNumber")
    @Mapping(source = "service.nameEn", target = "serviceNameEn")
    @Mapping(source = "service.nameAr", target = "serviceNameAr")
    @Mapping(source = "providerResponseMessage", target = "providerResponseMessage")
    @Mapping(target = "bookingDate", expression = "java(serviceRequest.getBookingDateTime().toLocalDate())")
    @Mapping(target = "bookingTime", expression = "java(serviceRequest.getBookingDateTime().toLocalTime())")
    TouristBookingResponseDto toTouristBookingResponseDto(ServiceRequest serviceRequest);



    @Named("toFullUrl")
    default String toFullUrl(ServiceRequest serviceRequest) {
        if (serviceRequest.getPlace().getMainImageUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/place_photos/")
                .path(serviceRequest.getPlace().getMainImageUrl())
                .toUriString();
    }
}

