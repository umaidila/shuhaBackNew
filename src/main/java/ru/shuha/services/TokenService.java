package ru.shuha.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shuha.db.entities.RefreshTokenEntity;
import ru.shuha.db.entities.UserEntity;
import ru.shuha.db.repositories.RefreshTokenRepository;
import ru.shuha.db.repositories.UserRepository;
import ru.shuha.exceptions.ElementNotFoundException;
import ru.shuha.exceptions.PreconditionFailedException;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, username);
    }


    private String createAccessToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()
                        + 1000 // milli
                        * 60   // sec
                        * 10   // min
                ))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String updateTokenByAccess(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ElementNotFoundException("Refresh token not found"));
        if (refreshToken.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new PreconditionFailedException(refreshToken.getToken() + " Refresh token is expired. Please make a new login..!");
        }

        UserEntity user = refreshToken.getUser();
        return generateAccessToken(user.getLogin());
    }

    public String generateRefreshToken(UserEntity user) {
        refreshTokenRepository.deleteAllByUser(user);
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (refreshTokenRepository.existsByToken(token));

        RefreshTokenEntity newRefreshToken = RefreshTokenEntity.builder()
                .user(user)
                .expirationDate(Instant.now().plusSeconds(
                        60L    // sec
                        * 60   // min
                        * 24   // hour
                        * 30   // day
                ))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(newRefreshToken).getToken();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
