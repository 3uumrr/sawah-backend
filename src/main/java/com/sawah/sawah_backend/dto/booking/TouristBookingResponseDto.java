package com.sawah.sawah_backend.dto.booking;

import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.enums.VehicleType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TouristBookingResponseDto(
        Long bookingId,
        String placeNameEn,
        String placeNameAr,
        String placeImageUrl,
        String governorateEn,
        String governorateAr,
        Long providerId,
        String providerFullName,
        String providerPhoneNumber,
        String serviceNameEn,
        String serviceNameAr,
        String providerResponseMessage,
        LocalDate bookingDate,
        LocalTime bookingTime,
        Integer durationHours,
        Integer durationDays,
        Integer numberOfPeople,
        String translationLanguage,
        VehicleType preferredVehicleType,
        String additionalNotes,
        Double pickupLatitude,
        Double pickupLongitude,
        Double totalPrice,
        ServiceRequestStatus status
) {
}
