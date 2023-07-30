package com.coinlift.backend.services.users;

import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Extracts the email from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The email extracted from the token's claims.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the provided JWT token.
     *
     * @param token          The JWT token.
     * @param claimsResolver The function to resolve the claim from the token's claims.
     * @param <T>            The type of the claim to extract.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a new JWT token for the provided user details.
     *
     * @param userDetails The user details to include in the token's claims.
     * @return The generated JWT token.
     */
    public String generateToken(MyUserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a new JWT token for the provided user details and additional claims.
     *
     * @param extractClaims Additional claims to include in the token's claims.
     * @param userDetails  The user details to include in the token's claims.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extractClaims, MyUserDetails userDetails) {
        long jwtExpiration = 7 * 24 * 60 * 60 * 1000;
        extractClaims.put("userId", userDetails.user().getId());
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the provided JWT token is valid for the given user details.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to validate against the token.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, MyUserDetails userDetails) {
        final String username = extractEmail(token);
        boolean isValid = tokenRepository.isValidToken(token);
        return (username.equals(userDetails.getUsername()) && isValid && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
