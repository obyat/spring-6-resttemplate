package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerSearchRequest;
import guru.springframework.spring7resttemplate.model.RestPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private static final String BEER_PATH = "/api/v1/beer";
    private static final String BEER_ID = "/{beerId}";

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers(BeerSearchRequest request) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder builder = buildUri(request);

        ResponseEntity<RestPageImpl> response =
                restTemplate.getForEntity(
                        builder.toUriString(),
                        RestPageImpl.class
                );

        return response.getBody();
    }


    @Override
    public BeerDTO getBeerById(UUID beerId) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(BEER_PATH + BEER_ID, BeerDTO.class, beerId);
    }


    @Override
    public BeerDTO createBeer(BeerDTO beerDTO) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        URI uri = restTemplate.postForLocation(BEER_PATH, beerDTO);

        if (uri == null) {
            throw new IllegalStateException("Server did not return a Location header");
        }

        BeerDTO createdBeer = restTemplate.getForObject(uri, BeerDTO.class);

        if (createdBeer == null) {
            throw new IllegalStateException("Server returned no beer");
        }

        String path = uri.getPath();
        String idString = path.substring(path.lastIndexOf('/') + 1);

        createdBeer.setId(UUID.fromString(idString));

        return createdBeer;
    }


    @Override
    public BeerDTO updateBeer(BeerDTO beerDTO) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        assert beerDTO.getId() != null;
        restTemplate.put(
                BEER_PATH + "/" + beerDTO.getId(),
                beerDTO
        );

        return getBeerById(beerDTO.getId());
    }


    @Override
    public void deleteBeer(UUID beerID) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.delete(BEER_PATH + BEER_ID, beerID);
    }


    private UriComponentsBuilder buildUri(BeerSearchRequest request) {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromPath(BEER_PATH);

        Map<String, Object> params = new LinkedHashMap<>();

        params.put("beerName", request.getBeerName());
        params.put("beerStyle", request.getBeerStyle());
        params.put("showInventory", request.getShowInventory());
        params.put("page", request.getPageNumber());
        params.put("size", request.getPageSize());

        params.forEach((key, value) -> {
            if (value != null) {
                builder.queryParam(key, value);
            }
        });

        return builder;
    }
}