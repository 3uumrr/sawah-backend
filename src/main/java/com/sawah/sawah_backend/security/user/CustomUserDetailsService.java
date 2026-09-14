package com.sawah.sawah_backend.security.user;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.ProviderRepository;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.service.provider.ProviderService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final ProviderService providerService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username){

        User user = userService.findUserByEmailWithRoles(username);

        boolean isProvider = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equalsIgnoreCase("PROVIDER"));

        if (isProvider) {
            ProviderStatus status = providerService.getProviderStatusByUserIdSafe(user.getId());
            return new CustomUserDetails(user, status);

        }

       return new CustomUserDetails(user);

    }
}
