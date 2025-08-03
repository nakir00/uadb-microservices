package uadb.location.logement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;
import uadb.location.logement.model.Chambre.Chambre;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String url;
    @Enumerated(EnumType.STRING)
    private Type type;
    private  String description;
    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creeLe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chambre_id", nullable = false)
    @JsonBackReference
    private Chambre chambre;

    public Media(String url, Type type, String description) {
        this.url = url;
        this.type = type;
        this.description = description;
    }

    public enum Type {
        PHOTO, VIDEO
    }

    public static InfoMediaResponse toInfoMediaResponse(Media media){
        return new InfoMediaResponse(media.getId(), media.getUrl(),media.getType(), media.getDescription(), media.getCreeLe());
    }

}
