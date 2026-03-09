package com.sawah.sawah_backend.security.user;

import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(username).orElseThrow(()
                -> new UsernameNotFoundException("User Not Found"));

       return new CustomUserDetails(user);
    }
}
