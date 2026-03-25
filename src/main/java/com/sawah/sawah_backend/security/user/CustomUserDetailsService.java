package com.sawah.sawah_backend.security.user;

import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username){

        User user = userService.findUserByEmailWithRoles(username);

       return new CustomUserDetails(user);

    }
}
