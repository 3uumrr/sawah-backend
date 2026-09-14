package com.sawah.sawah_backend.security.user;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.UserAccStatus;
import com.sawah.sawah_backend.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final ProviderStatus providerStatus;

    public CustomUserDetails(User user){
        this.user = user;
        this.providerStatus = null;
    }

    public CustomUserDetails(User user, ProviderStatus providerStatus) {
        this.user = user;
        this.providerStatus = providerStatus;
    }

    public Long getId() {
        return user.getId();
    }
    public ProviderStatus getProviderStatus() {
        return providerStatus;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public boolean isProfileComplete() {
        return user.getIsProfileComplete();
    }

    public UserAccStatus getAccountStatus() {
        return user.getAccountStatus();
    }

}
