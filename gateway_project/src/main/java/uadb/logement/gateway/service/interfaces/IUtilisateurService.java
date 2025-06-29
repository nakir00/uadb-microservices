package uadb.logement.gateway.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import uadb.logement.gateway.dto.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserRequest;
import uadb.logement.gateway.model.Utilisateur;
import uadb.logement.gateway.service.returnedValues.UserConnected;

import java.util.Optional;

public interface IUtilisateurService extends UserDetailsService {

    public Optional<UserConnected> authenticateUser(AuthenticateUserRequest authenticateUserRequest);

    public Optional<UserConnected> registerUser(RegisterUserRequest registerUserRequest);

}
