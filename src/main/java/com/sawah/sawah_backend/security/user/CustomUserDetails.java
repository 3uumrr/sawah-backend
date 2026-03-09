package com.sawah.sawah_backend.security.user;

import com.sawah.sawah_backend.enums.UserAccStatus;
import com.sawah.sawah_backend.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }


    public Long getId() {
        return user.getId();
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
