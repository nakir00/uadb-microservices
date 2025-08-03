package uadb.logement.gateway.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uadb.logement.gateway.model.Utilisateur;
import uadb.logement.gateway.repository.UtilisateurRepository;
import uadb.logement.gateway.service.interfaces.IUtilisateurService;

import java.util.Optional;

@Service
public class UtilisateurService implements IUtilisateurService {

    public final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    @Override
    public Utilisateur GetUtilisateurById(Long id) {
        if ( id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
        }

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        if (utilisateurOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");

        return  utilisateurOptional.get();
    }
}
