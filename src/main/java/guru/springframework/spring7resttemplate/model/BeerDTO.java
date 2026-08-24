package guru.springframework.spring7resttemplate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeerDTO {

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @UuidGenerator
    private UUID id;


    private Integer version;

    private String beerName;

    private String beerStyle;

    private String upc;

    private Integer quantityOnHand;

    private BigDecimal price;

    private OffsetDateTime createdDate;

    private OffsetDateTime lastModifiedDate;

    /**
     * Spring Data REST's HAL representation omits the entity ID from the payload,
     * but exposes it in the resource's self link.
     */
    @JsonSetter("_links")
    public void setHalLinks(HalLinks links) {
        if (links == null || links.self() == null || links.self().href() == null) {
            return;
        }

        try {
            String path = URI.create(links.self().href()).getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);
            setId(UUID.fromString(id));
        } catch (IllegalArgumentException ignored) {
            // Keep the ID unset when a response does not expose a UUID self link.
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record HalLinks(@JsonProperty("self") HalLink self) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record HalLink(@JsonProperty("href") String href) {
    }
}
