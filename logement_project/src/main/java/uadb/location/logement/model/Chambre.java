package uadb.location.logement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long maisonId;
    private String titre;
    private String description;
    private String taille;
    private String type;
    private boolean meublee;
    private boolean salleDeBain;
    private boolean disponible;
    private BigDecimal prix;
    private LocalDateTime creeLe;

    @ManyToOne
    @JoinColumn(name = "maison_id", nullable = false)
    private Maison maison;
}
