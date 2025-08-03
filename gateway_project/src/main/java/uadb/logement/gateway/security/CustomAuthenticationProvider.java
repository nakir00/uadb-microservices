package uadb.logement.gateway.security;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import uadb.logement.gateway.service.AuthService;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final AuthService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(AuthService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(@NotNull Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();
        log.info("Attempting authentication for user: {}", email);

        try {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            if (user == null) {
                throw new UsernameNotFoundException("Utilisateur introuvable");
            }

            // Use passwordEncoder to check encoded password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            log.info("Authentication successful for user: {}", email);

            log.info("role : {}", user.getAuthorities().stream().findFirst().get().toString());

            return UsernamePasswordAuthenticationToken.authenticated(user, password, user.getAuthorities());
        } catch (UsernameNotFoundException e) {
            log.error("User not found: {}", email);
            throw e;
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", email, e);
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public boolean supports(@NotNull Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}