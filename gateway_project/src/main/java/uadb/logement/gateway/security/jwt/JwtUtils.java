package uadb.logement.gateway.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import uadb.logement.gateway.controller.AuthController;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.refreshTokenExpirationMs}")
    private int refreshTokenExpirationMs;

    @Value("${app.jwtCookieName}")
    private String jwtCookieName;

    @Value("${app.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Génération du cookie JWT (Access Token)
    public ResponseCookie generateJwtCookie(UserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtCookieName, jwt, "/", jwtExpirationMs / 1000);
    }

    // Génération du cookie Refresh Token
    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return ResponseCookie.from(refreshTokenCookieName, refreshToken)
                .path("/api/auth") // Path plus restrictif pour le refresh token
                .maxAge(refreshTokenExpirationMs / 1000)
                .httpOnly(true)
                .secure(true) // HTTPS uniquement
                .sameSite("None") // Protection CSRF plus stricte
                .build();
    }

    // Cookie générique
    private ResponseCookie generateCookie(String name, String value, String path, long maxAge) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true) // Activer en production avec HTTPS
                .sameSite("None") // Protection CSRF
                .build();
    }

    // Extraction du JWT depuis les cookies
    public String getJwtFromCookies(HttpServletRequest request) {
        System.out.println(jwtCookieName);
        return getCookieValueByName(request, jwtCookieName);
    }

    // Extraction du Refresh Token depuis les cookies
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, refreshTokenCookieName);
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {

        Cookie cookie = WebUtils.getCookie(request, name);

        if (cookie != null) {
            System.out.println(cookie.getValue());
            return cookie.getValue();
        } else {
            System.out.println("aucun token retrouvé");
            return null;
        }
    }

    // Génération de l'Access Token
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Génération du Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshTokenExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .claim("type", "refresh") // Identifier le type de token
                .compact();
    }

    // Validation du token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Validation spécifique du refresh token
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Vérifier que c'est bien un refresh token
            return "refresh".equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Cookies de nettoyage (logout)
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null)
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, null)
                .path("/api/auth")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }
}