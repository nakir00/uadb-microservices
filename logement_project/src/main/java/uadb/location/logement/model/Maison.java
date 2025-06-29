package uadb.location.logement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Maison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long proprietaireId;
    private String adresse;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String text;
    private LocalDateTime creeLe;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chambre", cascade = CascadeType.ALL)
    private List<Chambre> chambres;
}
