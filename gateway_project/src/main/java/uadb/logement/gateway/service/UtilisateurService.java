package uadb.logement.gateway.service;

import lombok.extern.slf4j.Slf4j;
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
import uadb.logement.gateway.controller.UtilisateurController;
import uadb.logement.gateway.dto.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserRequest;
import uadb.logement.gateway.model.Utilisateur;
import uadb.logement.gateway.repository.UtilisateurRepository;
import uadb.logement.gateway.security.jwt.JwtUtils;
import uadb.logement.gateway.service.interfaces.IUtilisateurService;
import uadb.logement.gateway.service.returnedValues.UserConnected;

import java.util.Optional;

@Service
@Slf4j
public class UtilisateurService implements IUtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Use @Lazy to break circular dependency
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
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
        return utilisateur;
    }

   /* @Override
    public Optional<Utilisateur> authenticateUser(AuthenticateUserRequest authenticateUserRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateUserRequest.,
                            authenticateUserRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                Utilisateur user = (Utilisateur) authentication.getPrincipal();
                return Optional.of(user);
            }
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", authenticateUserRequest.getEmail(), e);
        }

        return Optional.empty();
    }*/

    @Override
    public Optional<UserConnected> authenticateUser(AuthenticateUserRequest authenticateUserRequest) {
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
    public Optional<UserConnected> registerUser(RegisterUserRequest registerUserRequest) {
        if (utilisateurRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new RuntimeException("User profile already exists for email: " + registerUserRequest.getEmail());
        }

        logger.info("Registering new user: {}", registerUserRequest.getEmail());

        Utilisateur userProfile = new Utilisateur();
        userProfile.setEmail(registerUserRequest.getEmail());
        userProfile.setNomUtilisateur(registerUserRequest.getNomUtilisateur());
        userProfile.setTelephone(registerUserRequest.getTelephone());
        userProfile.setCNI(registerUserRequest.getCNI());
        userProfile.setRole(Utilisateur.Role.LOCATAIRE);
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