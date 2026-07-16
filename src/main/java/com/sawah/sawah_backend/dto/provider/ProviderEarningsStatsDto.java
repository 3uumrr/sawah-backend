package com.sawah.sawah_backend.dto.provider;

import java.time.LocalDateTime;

public record ProviderEarningsStatsDto(
        Double totalEarnings,
        Double weeklyEarnings,
        Double monthlyEarnings,
        Long completedToursCount,
        Long cancelledToursCount
) {
}
