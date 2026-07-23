package com.sawah.sawah_backend.dto.provider;

import com.sawah.sawah_backend.dto.driverProfile.UpdateDriverVehicleDto;
import com.sawah.sawah_backend.dto.providerLanguage.UpdateProviderLanguageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record UpdateProviderProfileRequestDto(
                @NotBlank(message = "{user.firstname.required}") @Size(min = 2, max = 50, message = "{user.firstname.size}") String firstName,

                @NotBlank(message = "{user.lastname.required}") @Size(min = 2, max = 50, message = "{user.lastname.size}") String lastName,

                @NotBlank(message = "{user.email.required}") @Email(message = "{user.email.invalid}") String email,

                @NotBlank(message = "{user.phoneNumber.required}") String phoneNumber,

                @NotBlank(message = "{user.country.required}") String country,

                @NotBlank(message = "{user.gender.required}") String gender,

                @Size(max = 1000, message = "{provider.bio.maxLength}") String bio,

                @NotNull(message = "{provider.experienceYears.required}") @Min(value = 0, message = "{provider.experienceYears.invalid}") Integer experienceYears,

                @Min(value = 0, message = "{provider.ratePerHour.invalid}") BigDecimal ratePerHour,

                @Min(value = 0, message = "{provider.ratePerDay.invalid}") BigDecimal ratePerDay,

                @Valid List<UpdateProviderLanguageDto> languages,

                @Valid UpdateDriverVehicleDto vehicleDetails) {
}
