package uadb.logement.gateway.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uadb.logement.gateway.controller.AuthController;
import uadb.logement.gateway.service.AuthService;

import java.io.IOException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthTokenFilter(JwtUtils jwtUtils, AuthService authService) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            boolean tokenRefreshed = false;

            // Tentative d'authentification avec l'access token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                authenticateUser(jwt, request);
            }
            // Si l'access token est invalide/expiré, essayer le refresh token
            else if (jwt != null) {
                log.debug("Access token invalide ou expiré, tentative de refresh...");
                tokenRefreshed = attemptTokenRefresh(request, response);
            }

            // Si aucun token valide et pas de refresh réussi
            if (SecurityContextHolder.getContext().getAuthentication() == null && !tokenRefreshed) {
                log.debug("Aucune authentification valide trouvée");
            }

        } catch (Exception e) {
            log.error("Erreur lors de l'authentification: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authentifie l'utilisateur avec un token JWT valide
     */
    private void authenticateUser(String jwt, HttpServletRequest request) {
        try {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            UserDetails userDetails = authService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("Utilisateur authentifié: {}", username);
        } catch (Exception e) {
            log.error("Erreur lors de l'authentification de l'utilisateur: {}", e.getMessage());
        }
    }

    /**
     * Tente de rafraîchir les tokens en utilisant le refresh token
     */
    private boolean attemptTokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = jwtUtils.getRefreshTokenFromCookies(request);

            if (refreshToken == null) {
                log.debug("Aucun refresh token trouvé");
                return false;
            }

            if (!jwtUtils.validateRefreshToken(refreshToken)) {
                log.debug("Refresh token invalide ou expiré");
                // Nettoyer les cookies invalides
                clearAuthCookies(response);
                return false;
            }

            // Extraire le username du refresh token
            String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
            UserDetails userDetails = authService.loadUserByUsername(username);

            // Générer de nouveaux tokens
            ResponseCookie newAccessTokenCookie = jwtUtils.generateJwtCookie(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(username);
            ResponseCookie newRefreshTokenCookie = jwtUtils.generateRefreshJwtCookie(newRefreshToken);

            // Ajouter les nouveaux cookies à la réponse
            response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

            // Authentifier l'utilisateur avec le nouveau token
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Tokens rafraîchis automatiquement pour l'utilisateur: {}", username);
            return true;

        } catch (Exception e) {
            log.error("Erreur lors du rafraîchissement automatique des tokens: {}", e.getMessage());
            clearAuthCookies(response);
            return false;
        }
    }

    /**
     * Nettoie les cookies d'authentification en cas d'erreur
     */
    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie cleanAccessToken = jwtUtils.getCleanJwtCookie();
        ResponseCookie cleanRefreshToken = jwtUtils.getCleanRefreshTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, cleanAccessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cleanRefreshToken.toString());

        log.debug("Cookies d'authentification nettoyés");
    }

    /**
     * Détermine si la requête nécessite une authentification
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Ne pas filtrer les endpoints publics
        return path.startsWith("/api/auth/signin") ||
                path.startsWith("/api/auth/signup") ||
                path.startsWith("/api/public/") ;//||
                //path.startsWith("/h2-console/") ||
                //path.equals("/favicon.ico");
    }
}