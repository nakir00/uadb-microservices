package uadb.location.logement.model.rendezVous;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponse;
import uadb.location.logement.model.Chambre.Chambre;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long locataireId;
    private LocalDateTime dateHeure;
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creeLe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chambre_id", nullable = false)
    @JsonBackReference
    private Chambre chambre;

    public RendezVous() {
    }

    public RendezVous(Long id, Long locataireId, LocalDateTime dateHeure, Statut statut, LocalDateTime creeLe, Chambre chambre) {
        this.id = id;
        this.locataireId = locataireId;
        this.dateHeure = dateHeure;
        this.statut = statut;
        this.creeLe = creeLe;
        this.chambre = chambre;
    }

    public Long id() {
        return id;
    }

    public RendezVous setId(Long id) {
        this.id = id;
        return this;
    }

    public Long locataireId() {
        return locataireId;
    }

    public RendezVous setLocataireId(Long locataireId) {
        this.locataireId = locataireId;
        return this;
    }

    public LocalDateTime dateHeure() {
        return dateHeure;
    }

    public RendezVous setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
        return this;
    }

    public Statut statut() {
        return statut;
    }

    public RendezVous setStatut(Statut statut) {
        this.statut = statut;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public RendezVous setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public Chambre chambre() {
        return chambre;
    }

    public RendezVous setChambre(Chambre chambre) {
        this.chambre = chambre;
        return this;
    }

    public enum Statut {
        EN_ATTENTE, CONFIRME, ANNULE
    }

    public static InfoRendezVousResponse toInfoRendezVousResponse(RendezVous rendezVous) {
        return new InfoRendezVousResponse(
                rendezVous.id(),
                rendezVous.locataireId(),
                rendezVous.dateHeure(),
                rendezVous.statut(),
                rendezVous.creeLe(),
                Chambre.toChambreResponse(rendezVous.chambre())
        );
    }
}
