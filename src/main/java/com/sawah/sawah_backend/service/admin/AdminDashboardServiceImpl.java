package com.sawah.sawah_backend.service.admin;

import com.sawah.sawah_backend.dto.dashboard.DashboardCounters;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.service.category.CategoryService;
import com.sawah.sawah_backend.service.place.PlaceService;
import com.sawah.sawah_backend.service.provider.ProviderService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserService userService;
    private final PlaceService placeService;
    private final CategoryService categoryService;
    private final ProviderService providerService;

    @Override
    public DashboardCounters getOverviewData() {

        Long totalPlaces = placeService.placesCount();
        Long totalUsers = userService.countUsers();
        Long totalCategories = categoryService.categoriesCount();
        Long totalProviders = providerService.countProviders();
        Long pendingApprovals = providerService.countProvidersByStatus(ProviderStatus.PENDING);

        return new DashboardCounters(totalUsers,totalProviders,pendingApprovals,totalPlaces,totalCategories);

    }
}
