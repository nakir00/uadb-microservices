package uadb.logement.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uadb.logement.gateway.controller.interfaces.IAuthController;
import uadb.logement.gateway.dto.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserResponse;
import uadb.logement.gateway.security.jwt.JwtUtils;
import uadb.logement.gateway.service.AuthService;
import uadb.logement.gateway.service.returnedValues.UserConnected;

import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthController implements IAuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }


    @Override
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(RegisterUserRequest registerUserRequest) {

        Optional<UserConnected> registerUserReturn = authService.registerUser(registerUserRequest);
        if (registerUserReturn.isPresent()) {
            ResponseCookie idCookie =ResponseCookie.from("user_id", registerUserReturn.get().getUser().getId().toString())
                    .path("/")
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getToken().toString())
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getRefresh().toString())
                    .header(HttpHeaders.SET_COOKIE, idCookie.toString())
                    .body(new RegisterUserResponse(
                            registerUserReturn.get().getUser().getId(),
                            registerUserReturn.get().getUser().getNomUtilisateur(),
                            registerUserReturn.get().getUser().getEmail(),
                            registerUserReturn.get().getUser().getCNI(),
                            registerUserReturn.get().getUser().getTelephone(),
                            registerUserReturn.get().getUser().getRole().toString()
                            ));
        }  else{
            return ResponseEntity.badRequest().body("impossible de creer l'utilisateur");
        }
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(AuthenticateUserRequest authenticateUserRequest) {
        Optional<UserConnected> registerUserReturn = authService.authenticateUser(authenticateUserRequest);

        if (registerUserReturn.isPresent()) {
            ResponseCookie idCookie =ResponseCookie.from("user_id", registerUserReturn.get().getUser().getId().toString())
                    .path("/")
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getToken().toString())
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getRefresh().toString())
                    .header(HttpHeaders.SET_COOKIE, idCookie.toString())
                    .body(new RegisterUserResponse(
                            registerUserReturn.get().getUser().getId(),
                            registerUserReturn.get().getUser().getNomUtilisateur(),
                            registerUserReturn.get().getUser().getEmail(),
                            registerUserReturn.get().getUser().getCNI(),
                            registerUserReturn.get().getUser().getTelephone(),
                            registerUserReturn.get().getUser().getRole().toString()
                    ));
        }  else{
            return ResponseEntity.badRequest().body("impossible d'authentifier l'utilisateur");
        }
    }

    @Override
    @GetMapping("/logout")
    public  ResponseEntity<?> logoutUser(){
        ResponseCookie cleanAccessTokenCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie cleanRefreshTokenCookie = jwtUtils.getCleanRefreshTokenCookie();
        ResponseCookie idCookie =ResponseCookie.from("user_id",null)
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanAccessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, cleanRefreshTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, idCookie.toString())
                .body("utilisateur deconnecté");
    }
}
