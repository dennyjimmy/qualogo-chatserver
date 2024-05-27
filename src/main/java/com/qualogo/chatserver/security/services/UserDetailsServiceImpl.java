package com.qualogo.chatserver.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qualogo.chatserver.models.User;
import com.qualogo.chatserver.repository.UserRepository;

/**
 * Service implementation for loading user-specific data.
 * This service is used by Spring Security to load user details during authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Loads the user details by username.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never {@code null})
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}