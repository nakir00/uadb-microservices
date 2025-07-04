package uadb.logement.gateway.controller.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import uadb.logement.gateway.dto.authenticateUser.AuthenticateUserRequest;
import uadb.logement.gateway.dto.registerUser.RegisterUserRequest;


public interface IAuthController {

    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest);
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticateUserRequest authenticateUserRequest);
    public ResponseEntity<?> logoutUser();
}
