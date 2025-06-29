package uadb.logement.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uadb.logement.gateway.controller.interfaces.IUtilisateurController;
import uadb.logement.gateway.dto.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserResponse;
import uadb.logement.gateway.service.UtilisateurService;
import uadb.logement.gateway.service.returnedValues.UserConnected;

import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class UtilisateurController implements IUtilisateurController {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(RegisterUserRequest registerUserRequest) {

        Optional<UserConnected> registerUserReturn = utilisateurService.registerUser(registerUserRequest);
        if (registerUserReturn.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getToken().toString())
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getRefresh().toString())
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
        Optional<UserConnected> registerUserReturn = utilisateurService.authenticateUser(authenticateUserRequest);
        if (registerUserReturn.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getToken().toString())
                    .header(HttpHeaders.SET_COOKIE, registerUserReturn.get().getRefresh().toString())
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

    @GetMapping("/")
    public  ResponseEntity<?> test(){
        return ResponseEntity.ok().body("je vais bien");
    }
}
