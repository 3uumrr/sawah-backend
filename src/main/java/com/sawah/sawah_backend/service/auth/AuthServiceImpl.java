package com.sawah.sawah_backend.service.auth;

import com.sawah.sawah_backend.dto.auth.ResetPasswordRequest;
import com.sawah.sawah_backend.dto.auth.GoogleAuthRequestDto;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.RoleName;
import com.sawah.sawah_backend.enums.UserAccStatus;
import com.sawah.sawah_backend.exceptions.AccountNotActiveException;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.helper.EmailVerificationService;
import com.sawah.sawah_backend.helper.OtpGenerator;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.requests.LoginRequest;
import com.sawah.sawah_backend.response.AuthResponse;
import com.sawah.sawah_backend.security.jwt.JwtUtils;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.email.EmailService;
import com.sawah.sawah_backend.service.provider.ProviderService;
import com.sawah.sawah_backend.service.role.RoleService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ProviderService providerService;
    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getAccountStatus() == UserAccStatus.INACTIVE) {
            throw new AccountNotActiveException("account.is.inactive");
        }

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return buildAuthResponse(userDetails, authentication);
    }

    @Override
    @Transactional
    public AuthResponse loginWithGoogle(GoogleAuthRequestDto request) {
        if (request.accountType() != RoleName.TOURIST && request.accountType() != RoleName.PROVIDER) {
            throw new BadRequestException("google.accountType.invalid");
        }

        GoogleUserInfo googleUserInfo = googleTokenVerifier.verify(request.idToken());
        User user = findOrCreateGoogleUser(googleUserInfo, request.accountType());

        if (user.getAccountStatus() == UserAccStatus.INACTIVE) {
            throw new AccountNotActiveException("account.is.inactive");
        }

        CustomUserDetails userDetails = toCustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return buildAuthResponse(userDetails, authentication);
    }

    @Override
    public void signUp(UserInputDto request, String accountType) {
      String emailStatus = emailVerificationService.getEmailStatus(request.email());

       if (!"valid".equalsIgnoreCase(emailStatus)) {
           throw new BadRequestException("email.invalid");
        }

        userService.addUser(request, accountType);
    }

    private User findOrCreateGoogleUser(GoogleUserInfo googleUserInfo, RoleName accountType) {
        Optional<User> existingUser = userRepository.findByEmailWithRoles(googleUserInfo.email());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getAuthProvider() == null || user.getProviderSubject() == null) {
                user.setAuthProvider("GOOGLE");
                user.setProviderSubject(googleUserInfo.subject());
                return userRepository.save(user);
            }
            return user;
        }

        User user = User.builder()
                .firstName(googleUserInfo.firstName())
                .lastName(googleUserInfo.lastName())
                .email(googleUserInfo.email())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .authProvider("GOOGLE")
                .providerSubject(googleUserInfo.subject())
                .roles(Set.of(roleService.findByName(accountType)))
                .accountStatus(UserAccStatus.ACTIVE)
                .isProfileComplete(false)
                .build();

        return userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(CustomUserDetails userDetails, Authentication authentication) {
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        ProviderStatus providerStatus = null;
        String rejectionReason = null;

        if (roles.contains("ROLE_PROVIDER") && userDetails.isProfileComplete()) {
            Provider provider = providerService.getProviderByUserId(userDetails.getId());

            providerStatus = provider.getAccountStatus();
            rejectionReason = provider.getRejectionReason();

            if (provider.getAccountStatus() == ProviderStatus.REJECTED) {
                return new AuthResponse(null, null, null, roles, null, providerStatus, rejectionReason);
            }
        }

        String token = jwtUtils.generateTokenForUser(authentication);

        return new AuthResponse(null, token, userDetails.isProfileComplete(), roles, null, providerStatus, null);
    }

    private CustomUserDetails toCustomUserDetails(User user) {
        boolean isProvider = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.PROVIDER);

        if (isProvider) {
            return new CustomUserDetails(user, providerService.getProviderStatusByUserIdSafe(user.getId()));
        }

        return new CustomUserDetails(user);
    }

    @Override
    public void initiatePasswordReset(String email, Locale locale) {
        if (!userService.existByEmail(email)) {
            throw new ResourceNotFoundException("user.not.found");
        }

        String code = OtpGenerator.generateOtp(6);

        emailService.sendVerificationCode(email, code, locale);

        saveOtpInRedis(email, code);

    }

    public void resetPassword(ResetPasswordRequest request) {

        User user = userService.findUserByEmail(request.email());

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new RuntimeException("password.same.as.old");
        }

        if (!validateAndConsumeOtp(request.email(), request.otp())) {
            throw new ResourceNotFoundException("otp.invalid.or.expired");
        }

        userService.resetPassword(user.getEmail(), request.newPassword());
    }

    public void saveOtpInRedis(String email, String otp) {

        String key = "OTP:" + email;

        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(10));
    }

    public boolean validateAndConsumeOtp(String email, String userInputOtp) {
        String key = "OTP:" + email;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(userInputOtp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

}
