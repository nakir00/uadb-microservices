package uadb.logement.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uadb.logement.gateway.controller.AuthController;
import uadb.logement.gateway.dto.auth.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.auth.registerUser.RegisterUserRequest;
import uadb.logement.gateway.model.Utilisateur;
import uadb.logement.gateway.repository.UtilisateurRepository;
import uadb.logement.gateway.security.jwt.JwtUtils;
import uadb.logement.gateway.service.interfaces.IAuthService;
import uadb.logement.gateway.service.returnedValues.UserConnected;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Use @Lazy to break circular dependency
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur n'existe pas: " + username));


        logger.debug("User found: {}", utilisateur.getEmail());
        logger.debug("User role: {}", utilisateur.getRole());
        logger.debug("User authorities: {}", utilisateur.getAuthorities());
        return utilisateur;
    }

    @Override
    public Optional<UserConnected> authenticateUser(@NotNull AuthenticateUserRequest authenticateUserRequest) {
        if (!utilisateurRepository.existsByEmail(authenticateUserRequest.getEmail())) {
            throw new RuntimeException("User profile does not exists for email: " + authenticateUserRequest.getEmail());
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticateUserRequest.getEmail(),
                        authenticateUserRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Utilisateur userDetails = (Utilisateur) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshJwtCookie(refreshToken);

        return Optional.of(new UserConnected(jwtCookie, refreshTokenCookie, userDetails));
    }

    @Override
    public Optional<UserConnected> registerUser(@NotNull RegisterUserRequest registerUserRequest) {
        if (utilisateurRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new RuntimeException("User profile already exists for email: " + registerUserRequest.getEmail());
        }

        logger.info("Registering new user: {}", registerUserRequest.getEmail());

        Utilisateur userProfile = new Utilisateur();
        userProfile.setEmail(registerUserRequest.getEmail());
        userProfile.setNomUtilisateur(registerUserRequest.getNomUtilisateur());
        userProfile.setTelephone(registerUserRequest.getTelephone());
        userProfile.setCNI(registerUserRequest.getCNI());
        if (registerUserRequest.getRole().compareTo(Utilisateur.Role.ROLE_PROPRIETAIRE.name()) == 0) {
            userProfile.setRole(Utilisateur.Role.ROLE_PROPRIETAIRE);
        } else {
            userProfile.setRole(Utilisateur.Role.ROLE_LOCATAIRE);
        }
        userProfile.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        Utilisateur nouveauUser = utilisateurRepository.save(userProfile);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        nouveauUser.getEmail(),
                        registerUserRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Utilisateur userDetails = (Utilisateur) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getEmail());
        ResponseCookie refreshTokenCookie = jwtUtils.generateRefreshJwtCookie(refreshToken);

        return Optional.of(new UserConnected(jwtCookie, refreshTokenCookie, nouveauUser));
    }


}