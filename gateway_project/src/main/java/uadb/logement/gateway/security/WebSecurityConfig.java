package uadb.logement.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uadb.logement.gateway.security.jwt.AuthEntryPointJwt;
import uadb.logement.gateway.security.jwt.AuthTokenFilter;
import uadb.logement.gateway.security.jwt.JwtUtils;
import uadb.logement.gateway.service.UtilisateurService;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtUtils jwtUtils;

    public WebSecurityConfig(JwtUtils jwtUtils, AuthEntryPointJwt unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(utilisateurService, passwordEncoder);
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(UtilisateurService utilisateurService) {
        return new AuthTokenFilter(this.jwtUtils, utilisateurService);
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthenticationProvider authenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationProvider authenticationProvider, UtilisateurService utilisateurService) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        //.requestMatchers("/api/test/**").permitAll()
                        //.requestMatchers("/api/auth/users").authenticated()
                        .anyRequest().authenticated()
                );

        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        http.authenticationProvider(authenticationProvider);

        http.addFilterBefore(authenticationJwtTokenFilter(utilisateurService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}