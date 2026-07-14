package com.sawah.sawah_backend.dto.dashboard;

public record DashboardCounters(
        long totalUsers,
        long totalProviders,
        long pendingApprovals,
        long totalPlaces,
        long totalCategories
) {}
