package guru.springframework.spring7resttemplate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Embedded(
        List<BeerDTO> beer
) {
}