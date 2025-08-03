package uadb.logement.gateway.service.interfaces;

import uadb.logement.gateway.model.Utilisateur;

public interface IUtilisateurService {

    Utilisateur GetUtilisateurById(Long Id);
}
