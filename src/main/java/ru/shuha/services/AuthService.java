package ru.shuha.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shuha.exceptions.TokenValidationException;

@Service
@RequiredArgsConstructor
public class AuthService {



    public String getAuthLogin() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            throw new TokenValidationException("invalid token");
        }

    }
}
