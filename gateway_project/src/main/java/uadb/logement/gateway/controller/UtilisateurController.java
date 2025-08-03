package uadb.logement.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uadb.logement.gateway.dto.utilisateur.InfoUtilisateurResponse;
import uadb.logement.gateway.model.Utilisateur;
import uadb.logement.gateway.service.UtilisateurService;

@RequestMapping("/api/user")
@RestController
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("{id}")
    public ResponseEntity<InfoUtilisateurResponse> getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(Utilisateur.toInfoUtilisateurResponse(utilisateurService.GetUtilisateurById(id)));
    }
}
