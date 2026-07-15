package com.sawah.sawah_backend.response;

import com.sawah.sawah_backend.enums.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private Boolean isProfileComplete;
    private Set<String> roles;
    private LocalDateTime timestamp;
    private ProviderStatus providerStatus;
    private String rejectionReason;

}
