package ru.shuha.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.exceptions.TokenValidationException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public String getAuthLogin() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            throw new TokenValidationException("invalid token");
        }
    }

    public UserEntity getAuthUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByLogin(userDetails.getUsername()).orElseThrow();
        } else {
            throw new TokenValidationException("invalid token");
        }
    }
}
