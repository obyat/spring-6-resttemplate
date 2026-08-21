package guru.springframework.spring7resttemplate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerSearchRequest {

    private String beerName;

    private BeerStyle beerStyle;

    private Boolean showInventory;

    private Integer pageNumber;

    private Integer pageSize;
}