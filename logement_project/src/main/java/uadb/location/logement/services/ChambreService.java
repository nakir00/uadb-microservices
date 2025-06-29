package uadb.location.logement.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uadb.location.logement.repositories.ChambreRepository;
import uadb.location.logement.services.Interfaces.IChambreService;

@AllArgsConstructor
@Service
public class ChambreService implements IChambreService {

    private ChambreRepository chambreRepository;


}
