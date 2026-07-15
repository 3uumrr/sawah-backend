package com.sawah.sawah_backend.dto.booking;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingRequestDto(
        @NotNull(message = "booking.placeId.required")
        Long placeId,

        @NotNull(message = "booking.providerId.required")
        Long providerId,

        @NotNull(message = "booking.bookingDate.required")
        @FutureOrPresent(message = "booking.bookingDate.future")
        LocalDate bookingDate,

        @NotNull(message = "booking.bookingTime.required")
        LocalTime bookingTime,

        @NotNull(message = "booking.numberOfPeople.required")
        @Min(value = 1, message = "booking.numberOfPeople.min")
        Integer numberOfPeople,

        @Positive(message = "booking.durationHours.positive")
        Integer durationHours,

        @Positive(message = "booking.durationDays.positive")
        Integer durationDays,

        @Size(max = 500, message = "booking.additionalNotes.max")
        String additionalNotes,

        Double pickupLatitude,

        Double pickupLongitude
) {}