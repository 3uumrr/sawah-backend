package com.sawah.sawah_backend.service.driverProfile;

import com.sawah.sawah_backend.models.DriverProfile;

public interface DriverProfileService {
    void addDriverProfile(DriverProfile driverProfileInput);
    DriverProfile getDriverProfileById(Long id);

    DriverProfile getDriverProfileByProviderId(Long providerId);

    void updateDriverProfile(DriverProfile driverProfile);
    void deleteById(Long id);
}
