package com.sawah.sawah_backend.service.admin;

import com.sawah.sawah_backend.dto.dashboard.DashboardCounters;

public interface AdminDashboardService {
    DashboardCounters getOverviewData();
}
