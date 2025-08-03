package uadb.location.logement.dto.controller.mediaController;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.logement.model.Media;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InfoMediaResponse(Long id,
                                String url,
                                Media.Type type,
                                String description,
                                LocalDateTime creeLe) {
}
