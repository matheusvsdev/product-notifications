package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Role;
import com.matheusvsdev.product_notifications.domain.User;
import com.matheusvsdev.product_notifications.projections.UserDetailsProjection;
import com.matheusvsdev.product_notifications.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }
        User user = new User();
        user.setEmail(result.getFirst().getUsername());
        user.setPassword(result.getFirst().getPassword());

        for (UserDetailsProjection userDetails : result) {
            user.addRole(new Role(userDetails.getRoleId(), userDetails.getAuthorities()));
        }
        return user;
    }
}
