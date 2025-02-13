package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.User;
import com.matheusvsdev.product_notifications.dto.AuthenticationDTO;
import com.matheusvsdev.product_notifications.dto.LoginResponseDTO;
import com.matheusvsdev.product_notifications.dto.ResponseUserDTO;
import com.matheusvsdev.product_notifications.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User authenticated() {
        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticator.getPrincipal();
        String username = jwt.getClaim("username");
        return userRepository.findByEmail(username);
    }

    @Transactional
    public LoginResponseDTO login(AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        String type = "Bearer";
        Instant expiresAt = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC);

        return new LoginResponseDTO(token, type, expiresAt);
    }

    public void validateSelfOrAdmin(Long userId) {
        User me = authenticated();
        if (me.hasRole("ROLE_ADMIN")) {
            return;
        }
        if (!me.getId().equals(userId)) {
            throw new UsernameNotFoundException("Acesso negado. Deve ser o próprio usuário ou admin");
        }
    }

    @Transactional(readOnly = true)
    public ResponseUserDTO getMe() {
        User user = authenticated();
        return new ResponseUserDTO(user);
    }
}
