package com.sawah.sawah_backend.dto.booking;

import com.sawah.sawah_backend.enums.ServiceRequestStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ProviderBookingResponseDto(
        Long bookingId,
        String placeNameEn,
        String placeNameAr,
        String placeImageUrl,
        String governorateAr,
        String governorateEn,
        String touristFullName,
        String touristPhoneNumber,
        String serviceNameEn,
        String serviceNameAr,
        LocalDate bookingDate,
        LocalTime bookingTime,
        Integer durationHours,
        Integer durationDays,
        Integer numberOfPeople,
        Double totalPrice,
        String additionalNotes,
        Double pickupLatitude,
        Double pickupLongitude,
        ServiceRequestStatus status
) {}



