package uadb.location.contrat.model.probleme;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeWORelationsResponse;
import uadb.location.contrat.dto.controller.problemeController.saveProbleme.CreateProblemeRequest;
import uadb.location.contrat.model.contrat.Contrat;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "problemes")
public class Probleme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contrat_id", nullable = false)
    private Long contratId;

    @Column(name = "signale_par")
    private Long signalePar;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "responsable")
    @Enumerated(EnumType.STRING)
    private Responsable responsable;

    @Column(name = "resolu")
    private Boolean resolu;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id", insertable = false, updatable = false)
    @JsonBackReference
    private Contrat contrat;

    public Long id() {
        return id;
    }

    public Probleme setId(Long id) {
        this.id = id;
        return this;
    }

    public Long contratId() {
        return contratId;
    }

    public Probleme setContratId(Long contratId) {
        this.contratId = contratId;
        return this;
    }

    public Long signalePar() {
        return signalePar;
    }

    public Probleme setSignalePar(Long signalePar) {
        this.signalePar = signalePar;
        return this;
    }

    public String description() {
        return description;
    }

    public Probleme setDescription(String description) {
        this.description = description;
        return this;
    }

    public Type type() {
        return type;
    }

    public Probleme setType(Type type) {
        this.type = type;
        return this;
    }

    public Responsable responsable() {
        return responsable;
    }

    public Probleme setResponsable(Responsable responsable) {
        this.responsable = responsable;
        return this;
    }

    public Boolean resolu() {
        return resolu;
    }

    public Probleme setResolu(Boolean resolu) {
        this.resolu = resolu;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public Probleme setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public Contrat contrat() {
        return contrat;
    }

    public Probleme setContrat(Contrat contrat) {
        this.contrat = contrat;
        return this;
    }

    public enum Responsable {
        LOCATAIRE,
        PROPRIETAIRE
    }

    public enum Type {
        PLOMBERIE,
        ELECTRICITE,
        AUTRE
    }

    // Constructeurs
    public Probleme() {
    }

    public static InfoProblemeWORelationsResponse toInfoProblemeWORelationsResponse(Probleme probleme) {
        return new InfoProblemeWORelationsResponse(
                probleme.id(),
                probleme.contratId(),
                probleme.signalePar(),
                probleme.description(),
                probleme.type(),
                probleme.responsable(),
                probleme.resolu(),
                probleme.creeLe()
        );
    }

    public static Probleme fromCreateProblemeRequest(CreateProblemeRequest createProblemeRequest) {
        return new Probleme()
                .setContratId(createProblemeRequest.contratId())
                .setResolu(createProblemeRequest.resolu())
                .setSignalePar(createProblemeRequest.signalePar())
                .setDescription(createProblemeRequest.description())
                .setType(createProblemeRequest.type())
                .setResponsable(createProblemeRequest.responsable());
    }

    public static InfoProblemeResponse toInfoProblemeResponse(Probleme probleme) {
        return new InfoProblemeResponse(
                probleme.id(),
                probleme.contratId(),
                probleme.signalePar(),
                probleme.description(),
                probleme.type(),
                probleme.responsable(),
                probleme.resolu(),
                probleme.creeLe(),
                Contrat.toInfoContratWORelationsResponse(probleme.contrat())
        );
    }

}
