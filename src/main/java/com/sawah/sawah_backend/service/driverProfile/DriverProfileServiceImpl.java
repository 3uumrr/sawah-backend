package com.sawah.sawah_backend.service.driverProfile;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.DriverProfile;
import com.sawah.sawah_backend.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverProfileServiceImpl implements DriverProfileService {

    private final DriverProfileRepository driverProfileRepository;


    @Override
    @Transactional
    public void addDriverProfile(DriverProfile driverProfile) {
        driverProfileRepository.save(driverProfile);
    }

    @Override
    public DriverProfile getDriverProfileById(Long id) {
       return driverProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DriverProfile.Not.Found"));
    }

    @Override
    public DriverProfile getDriverProfileByProviderId(Long providerId) {
        return driverProfileRepository.findByProviderId(providerId).orElse(null);
    }


    @Override
    @Transactional
    public void updateDriverProfile(DriverProfile driverProfile) {
        driverProfileRepository.save(driverProfile);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        DriverProfile driverProfile = getDriverProfileById(id);

        driverProfileRepository.delete(driverProfile);

    }


}
