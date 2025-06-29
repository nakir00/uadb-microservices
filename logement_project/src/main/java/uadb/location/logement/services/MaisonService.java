package uadb.location.logement.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uadb.location.logement.repositories.MaisonRepository;
import uadb.location.logement.services.Interfaces.IChambreService;
import uadb.location.logement.services.Interfaces.IMaisonService;

@AllArgsConstructor
@Service
public class MaisonService implements IMaisonService {

    private MaisonRepository maisonRepository;





}
