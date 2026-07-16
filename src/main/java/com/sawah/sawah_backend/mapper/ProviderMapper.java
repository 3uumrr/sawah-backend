package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.provider.*;
import com.sawah.sawah_backend.dto.driverProfile.UpdateDriverVehicleDto;
import com.sawah.sawah_backend.dto.providerLanguage.UpdateProviderLanguageDto;
import com.sawah.sawah_backend.dto.user.UserResponseDto;
import com.sawah.sawah_backend.enums.Gender;
import com.sawah.sawah_backend.dto.providerLanguage.ProviderLanguageResponseDto;
import com.sawah.sawah_backend.dto.driverProfile.DriverProfileResponseDto;
import com.sawah.sawah_backend.dto.service.ServiceProviderResponseDto;
import com.sawah.sawah_backend.models.DriverProfile;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.ProviderLanguage;
import com.sawah.sawah_backend.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Component
public class ProviderMapper {

    public List<ProviderWithoutDetailsResponseDto> providerWithoutDetailsResponseDtoList(List<Provider> providers) {
        if (providers == null) {
            return List.of();
        }
        return providers.stream()
                .map(this::providerWithoutDetailsResponseDto)
                .toList();
    }

    public ProviderWithoutDetailsResponseDto providerWithoutDetailsResponseDto(Provider provider) {
        return new ProviderWithoutDetailsResponseDto(provider.getId(),
                toFullName(provider.getUser()),
                toProviderFullUrl(provider.getUser()),
                provider.getBio(),provider.getRatePerHour(),
                provider.getRatePerDay(),
                provider.getAverageRating(),
                provider.getTotalReviews()
                );
    }

    public ProviderWithDetailsResponseDto providerWithDetailsResponseDto(Provider provider , ServiceProviderResponseDto service) {
        return new ProviderWithDetailsResponseDto(provider.getId(),
                toFullName(provider.getUser()),
                toProviderFullUrl(provider.getUser()),
                provider.getBio(),
                provider.getRatePerHour(),
                provider.getRatePerDay(),
                provider.getAverageRating(),
                provider.getTotalReviews(),
                provider.getExperienceYears(),
                service
        );
    }

    public ProviderForAdminWithoutDetailsDto providerForAdminWithoutDetailsDto(Provider provider) {
        return new ProviderForAdminWithoutDetailsDto(
                provider.getId(),
                toFullName(provider.getUser()),
                provider.getUser().getEmail(),
                toProviderFullUrl(provider.getUser()),
                provider.getIsAvailable(),
                provider.getAccountStatus(),
                provider.getService().getCode(),
                provider.getCreatedAt()
        );
    }

    public ProviderForAdminWithDetailsDto providerForAdminWithDetailsDto(
            Provider provider,
            UserResponseDto userResponseDto,
            List<ProviderLanguageResponseDto> languages,
            DriverProfileResponseDto driverProfile
    ) {
        return new ProviderForAdminWithDetailsDto(
                provider.getId(),
                provider.getBio(),
                provider.getExperienceYears(),
                provider.getRatePerHour(),
                provider.getRatePerDay(),
                provider.getIsAvailable(),
                provider.getAccountStatus(),
                provider.getCreatedAt(),
                provider.getUpdatedAt(),
                provider.getNationalId(),
                toNationalIdPhotoFullUrl(provider.getNationalIdFrontUrl()),
                toNationalIdPhotoFullUrl(provider.getNationalIdBackUrl()),
                provider.getApprovedAt(),
                provider.getRejectedAt(),
                provider.getRejectionReason(),
                provider.getAverageRating(),
                provider.getTotalReviews(),
                provider.getTotalBookings(),
                provider.getCompletedBookings(),
                userResponseDto,
                provider.getService().getCode(),
                languages,
                driverProfile
        );
    }

    public void updateEntityFromDto(UpdateProviderProfileRequestDto request, Provider provider) {
        if (request == null || provider == null) {
            return;
        }

        User user = provider.getUser();
        if (user != null) {
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(request.email());
            user.setPhoneNumber(request.phoneNumber());
            user.setCountry(request.country());

            if (request.gender() != null) {
                user.setGender(Gender.valueOf(request.gender().toUpperCase()));
            }
        }

        provider.setBio(request.bio());
        provider.setExperienceYears(request.experienceYears());
        provider.setRatePerHour(request.ratePerHour());
        provider.setRatePerDay(request.ratePerDay());
    }

    public ProviderProfileResponseDto providerProfileResponseDto(
            Provider provider,
            List<ProviderLanguage> languages,
            DriverProfile driverProfile
    ) {
        if (provider == null) {
            return null;
        }

        User user = provider.getUser();

        return new ProviderProfileResponseDto(
                user != null ? user.getFirstName() : null,
                user != null ? user.getLastName() : null,
                user != null ? user.getEmail() : null,
                toProviderFullUrl(user),
                user != null ? user.getPhoneNumber() : null,
                user != null ? user.getCountry() : null,
                user != null && user.getGender() != null ? user.getGender().name() : null,
                provider.getBio(),
                provider.getExperienceYears(),
                provider.getRatePerHour(),
                provider.getRatePerDay(),
                toUpdateProviderLanguageDtos(languages),
                toUpdateDriverVehicleDto(driverProfile)
        );
    }

    public ProviderForDashboardDto toProviderForDashboardDto(Provider provider) {
        return new ProviderForDashboardDto(provider.getId(),
                toFullName(provider.getUser()),
                toProviderFullUrl(provider.getUser()),
                provider.getService().getNameAr(),
                provider.getService().getNameEn(),
                provider.getIsAvailable(),
                provider.getAverageRating(),
                provider.getTotalReviews(),
                provider.getTotalBookings());
    }

    private List<UpdateProviderLanguageDto> toUpdateProviderLanguageDtos(List<ProviderLanguage> languages) {
        if (languages == null) {
            return List.of();
        }

        return languages.stream()
                .map(language -> new UpdateProviderLanguageDto(
                        language.getId(),
                        language.getLanguage() != null ? language.getLanguage().getCode() : null,
                        language.getProficiencyLevel()
                ))
                .toList();
    }

    private UpdateDriverVehicleDto toUpdateDriverVehicleDto(DriverProfile driverProfile) {
        if (driverProfile == null) {
            return null;
        }

        return new UpdateDriverVehicleDto(
                driverProfile.getId(),
                driverProfile.getVehicleType(),
                driverProfile.getVehicleModel()
        );
    }

    private String toProviderFullUrl(User user) {
        if (user == null || user.getProfilePictureUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(user.getProfilePictureUrl())
                .toUriString();
    }

    private String toNationalIdPhotoFullUrl(String photoName) {
        if (photoName == null || photoName.isBlank()) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/providers/national-ids/")
                .path(photoName)
                .toUriString();
    }

    private String toFullName(User user) {
        if (user == null) return null;
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
