package com.sawah.sawah_backend.service.provider;

import com.sawah.sawah_backend.dto.driverProfile.DriverProfileResponseDto;
import com.sawah.sawah_backend.dto.provider.*;
import com.sawah.sawah_backend.dto.providerLanguage.UpdateProviderLanguageDto;
import com.sawah.sawah_backend.dto.user.UserResponseDto;
import com.sawah.sawah_backend.mapper.UserMapper;
import com.sawah.sawah_backend.dto.providerLanguage.ProviderLanguageResponseDto;
import com.sawah.sawah_backend.dto.service.ServiceProviderResponseDto;
import com.sawah.sawah_backend.enums.Gender;
import com.sawah.sawah_backend.enums.PreferredLanguage;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.DriverProfileMapper;
import com.sawah.sawah_backend.mapper.ProviderLanguageMapper;
import com.sawah.sawah_backend.mapper.ProviderMapper;
import com.sawah.sawah_backend.models.*;
import com.sawah.sawah_backend.repository.ProviderRepository;
import com.sawah.sawah_backend.requests.ProviderLanguageRequest;
import com.sawah.sawah_backend.requests.RegisterProviderRequest;
import com.sawah.sawah_backend.service.booking.BookingService;
import com.sawah.sawah_backend.service.driverProfile.DriverProfileService;
import com.sawah.sawah_backend.service.fileStorage.FileStorageService;
import com.sawah.sawah_backend.service.language.LanguageService;
import com.sawah.sawah_backend.service.providerlanguage.ProviderLanguageService;
import com.sawah.sawah_backend.service.service.ServiceService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ServiceService serviceService;
    private final LanguageService languageService;
    private final ProviderLanguageService providerLanguageService;
    private final DriverProfileService driverProfileService;
    private final ProviderLanguageMapper  providerLanguageMapper;
    private final DriverProfileMapper driverProfileMapper;
    private final ProviderMapper providerMapper;
    private final UserMapper userMapper;
    private static final String PROVIDER_ID_PHOTO_DIR = "providers/national-ids/";
    private static final String USER_UPLOAD_DIR = "user_photos/";


    @Override
    @Transactional
    public void submitProviderApplication(Long userId, RegisterProviderRequest request, MultipartFile nationalIdFrontImage, MultipartFile nationalIdBackImage) {
        User user = userService.getUserById(userId);

        if (providerRepository.existsByUserId(userId)) {
            throw new RuntimeException("provider.already.exists");
        }

        if (providerRepository.existsByNationalId(request.nationalId())) {
            throw new RuntimeException("provider.nationalId.already.exists");
        }

        com.sawah.sawah_backend.models.Service service = serviceService.getByCode(request.serviceCode());

        String nationalIdFrontUrl;
        String nationalIdBackUrl;
        try {
            nationalIdFrontUrl = fileStorageService.storeFile(PROVIDER_ID_PHOTO_DIR, nationalIdFrontImage);
            nationalIdBackUrl = fileStorageService.storeFile(PROVIDER_ID_PHOTO_DIR, nationalIdBackImage);
        } catch (IOException e) {
            throw new RuntimeException("provider.nationalId.upload.failed");
        }

        Provider provider = Provider.builder()
                .nationalId(request.nationalId())
                .nationalIdFrontUrl(nationalIdFrontUrl)
                .nationalIdBackUrl(nationalIdBackUrl)
                .experienceYears(request.experienceYears())
                .ratePerHour(request.ratePerHour())
                .ratePerDay(request.ratePerDay())
                .user(user)
                .service(service)
                .accountStatus(ProviderStatus.PENDING)
                .isAvailable(false)
                .build();

        provider = providerRepository.save(provider);

        if (service.getCode() == ServiceCode.TRANSLATOR && request.languageRequest() != null && !request.languageRequest().isEmpty()) {
                for (ProviderLanguageRequest langRequest : request.languageRequest()) {
                    if (langRequest.languageCode() != null && langRequest.languageLevel() != null) {
                        Long languageId = languageService.getByCode(langRequest.languageCode()).getId();
                        providerLanguageService.create(
                                langRequest.languageLevel(),
                                provider.getId(),
                                languageId
                        );
                    }
                }
        }
        else if (service.getCode() == ServiceCode.DRIVER) {
            if (request.vehicleType() == null) {
                throw new RuntimeException("driver.vehicleType.required");
            }

            if (request.vehicleModel() == null || request.vehicleModel().isBlank()) {
                throw new RuntimeException("driver.vehicleModel.required");
            }

            DriverProfile driverProfile = DriverProfile.builder()
                    .vehicleType(request.vehicleType())
                    .vehicleModel(request.vehicleModel())
                    .vehicleCapacity(request.vehicleType().getCapacity())
                    .provider(provider)
                    .build();

            driverProfileService.addDriverProfile(driverProfile);
        }

    }

    @Override
    @Transactional
    public void completeProviderProfile(Long userId, CompleteProviderProfileDto profileData, MultipartFile providerImage) {

        User user = userService.getUserById(userId);

        Provider provider =  getProviderByUserId(userId);

        provider.setBio(profileData.bio());

        user.setPhoneNumber(profileData.phoneNumber());
        user.setCountry(profileData.country());
        if (profileData.gender() != null) {
            user.setGender(Gender.valueOf(profileData.gender().toUpperCase()));
        }
        if (profileData.preferredLanguage() != null) {
            user.setPreferredLanguage(PreferredLanguage.valueOf(profileData.preferredLanguage().toUpperCase()));
        }

        if (providerImage != null && !providerImage.isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(USER_UPLOAD_DIR, providerImage);
                user.setProfilePictureUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("photo.upload.failed");
            }
        }

        user.setIsProfileComplete(true);
    }

    @Override
    public Provider getProviderById(Long providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("provider.not.found"));
    }

    @Override
    public ProviderWithDetailsResponseDto getProviderWithDetailsById(Long providerId) {
        Provider provider = getProviderById(providerId);

        DriverProfileResponseDto driverProfile = driverProfileMapper
                .toDto(driverProfileService.getDriverProfileByProviderId(provider.getId()));

        List<ProviderLanguageResponseDto> providerLanguage = providerLanguageMapper
                .toDtoList(providerLanguageService.getByProviderId(provider.getId()));

        ServiceProviderResponseDto serviceProviderResponseDto = new ServiceProviderResponseDto(driverProfile, providerLanguage);

        return providerMapper.providerWithDetailsResponseDto(provider, serviceProviderResponseDto);
    }

    @Override
    public ProviderProfileResponseDto getProviderProfileByUserId(Long userId) {
        Provider provider = getProviderByUserId(userId);
        DriverProfile driverProfile = driverProfileService.getDriverProfileByProviderId(provider.getId());
        List<ProviderLanguage> providerLanguages = providerLanguageService.getByProviderId(provider.getId());

        return providerMapper.providerProfileResponseDto(provider, providerLanguages, driverProfile);
    }

    @Override
    public ProviderForAdminWithDetailsDto getProviderForAdminWithDetailsById(Long providerId) {
        Provider provider = providerRepository.findByIdWithUserAndService(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("provider.not.found"));

        UserResponseDto userResponseDto = userMapper.toResponseDto(provider.getUser());

        DriverProfileResponseDto driverProfile = driverProfileMapper
                .toDto(driverProfileService.getDriverProfileByProviderId(provider.getId()));

        List<ProviderLanguageResponseDto> providerLanguage = providerLanguageMapper
                .toDtoList(providerLanguageService.getByProviderId(provider.getId()));

        return providerMapper.providerForAdminWithDetailsDto(provider, userResponseDto, providerLanguage, driverProfile);
    }

    @Override
    public Provider getProviderByUserId(Long userId) {
        return providerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("provider.not.found"));
    }

    @Override
    public ProviderForDashboardDto getProviderForDashboardByUserId(Long userId) {

        Provider provider = getProviderByUserId(userId);

        return providerMapper.toProviderForDashboardDto(provider);

    }


    @Override
    public Page<Provider> getProviders(ServiceCode serviceCode,
                                       ProviderStatus status,
                                       Boolean isAvailable,
                                       Pageable pageable) {
        return providerRepository.findByServiceCodeAndStatus(serviceCode, status, isAvailable, pageable);
    }


    @Override
    public Page<ProviderForAdminWithoutDetailsDto> getProvidersForAdmin(
            ProviderStatus status,
            ServiceCode serviceCode,
            Boolean isAvailable,
            Pageable pageable) {
        return providerRepository.findProvidersForAdmin(status, serviceCode, isAvailable, pageable)
                .map(providerMapper::providerForAdminWithoutDetailsDto);
    }



    @Override
    @Transactional
    public void approveProvider(Long providerId) {
        Provider provider = getProviderById(providerId);
        provider.setAccountStatus(ProviderStatus.APPROVED);
        provider.setIsAvailable(true);
        provider.setApprovedAt(LocalDateTime.now());
        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public void rejectProvider(Long providerId, String rejectionReason) {
        Provider provider = getProviderById(providerId);
        provider.setAccountStatus(ProviderStatus.REJECTED);
        provider.setRejectedAt(LocalDateTime.now());
        provider.setRejectionReason(rejectionReason);
        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public void updateProviderAvailability(Long providerId) {

        Provider provider = getProviderByUserId(providerId);

        provider.setIsAvailable(!provider.getIsAvailable());

        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public void updateProvider(Long userId, UpdateProviderProfileRequestDto request, MultipartFile photo) {
        Provider provider = getProviderByUserId(userId);
        User user = provider.getUser();

        // 1. Map fields to Provider and User via Mapper
        providerMapper.updateEntityFromDto(request, provider);

        // 2. Handle Photo Upload
        if (photo != null && !photo.isEmpty()) {
            try {
                if (user.getProfilePictureUrl() != null) {
                    fileStorageService.deleteFile(USER_UPLOAD_DIR, user.getProfilePictureUrl());
                }
                String fileName = fileStorageService.storeFile(USER_UPLOAD_DIR, photo);
                user.setProfilePictureUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("photo.upload.failed");
            }
        }

        // 3. Update Languages
        List<ProviderLanguage> existingLanguages = providerLanguageService.getByProviderId(provider.getId());
        for (ProviderLanguage pl : existingLanguages) {
            providerLanguageService.delete(pl.getId());
        }

        providerLanguageService.flushChanges();

        if (request.languages() != null) {
            for (UpdateProviderLanguageDto langDto : request.languages()) {
                if (langDto.languageCode() != null && langDto.proficiencyLevel() != null) {
                    ProviderLanguage providerLanguage = providerLanguageMapper.toEntity(langDto);
                    providerLanguage.setProvider(provider);
                    providerLanguage.setLanguage(languageService.getByCode(langDto.languageCode()));
                    providerLanguageService.create(
                            providerLanguage.getProficiencyLevel(),
                            provider.getId(),
                            providerLanguage.getLanguage().getId()
                    );
                }
            }
        }

        // 4. Update Driver Vehicle Details
        if (provider.getService().getCode() == ServiceCode.DRIVER && request.vehicleDetails() != null) {
            DriverProfile driverProfile = driverProfileService.getDriverProfileByProviderId(provider.getId());
            if (driverProfile == null) {
                driverProfile = driverProfileMapper.toEntity(request.vehicleDetails());
                driverProfile.setProvider(provider);
                driverProfileService.addDriverProfile(driverProfile);
            } else {
                driverProfileMapper.updateEntityFromDto(request.vehicleDetails(), driverProfile);
                driverProfileService.updateDriverProfile(driverProfile);
            }
        }

        providerRepository.save(provider);
    }

    @Override
    public ProviderStatus getProviderStatusByUserIdSafe(Long userId) {
        return providerRepository.getProviderStatusByUserId(userId)
                .orElse(ProviderStatus.PENDING);
    }

    @Override
    public Long countProviders() {
        return providerRepository.count();
    }

    @Override
    public Long countProvidersByStatus(ProviderStatus providerStatus) {
        return providerRepository.countAllProvidersByProviderStatus(providerStatus);
    }
}
