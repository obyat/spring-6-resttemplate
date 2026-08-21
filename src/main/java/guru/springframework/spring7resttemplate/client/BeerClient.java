package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface BeerClient {

    Page<BeerDTO> listBeers(BeerSearchRequest request);

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO beerDTO);

    BeerDTO updateBeer(BeerDTO beerDTO);

    void deleteBeer(UUID beerID);
}