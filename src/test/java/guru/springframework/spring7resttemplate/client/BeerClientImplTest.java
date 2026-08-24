package guru.springframework.spring7resttemplate.client;

import guru.springframework.spring7resttemplate.model.BeerDTO;
import guru.springframework.spring7resttemplate.model.BeerSearchRequest;
import guru.springframework.spring7resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(properties = {
        "rest.template.user.name=user1",
        "rest.template.user.password=password"
})
// Calls actual server for tests
class BeerClientImplTest {

    @Autowired
    private BeerClient beerClient;

    @Test
    void listBeersNoFilters() {

        BeerSearchRequest request = BeerSearchRequest.builder()
                .build();

        Page<BeerDTO> beers = beerClient.listBeers(request);

        assertThat(beers, notNullValue());
        assertThat(beers.getContent(), notNullValue());
        assertThat(beers.getTotalElements(), greaterThan(0L));
    }



//    @Test
//    void listBeersByStyle() {
//
//        BeerSearchRequest request = BeerSearchRequest.builder()
//                .beerStyle(BeerStyle.ALE)
//                .build();
//
//        Page<BeerDTO> beers = beerClient.listBeers(request);
//
//        assertThat(
//                beers.stream()
//                        .map(BeerDTO::getBeerStyle)
//                        .toList(),
//                everyItem(containsStringIgnoringCase("ALE"))
//        );
//    }

    @Test
    void listBeersWithInventory() {

        BeerSearchRequest request = BeerSearchRequest.builder()
                .showInventory(true)
                .build();

        Page<BeerDTO> beers = beerClient.listBeers(request);

        assertThat(beers, notNullValue());
        assertThat(beers.getContent(), notNullValue());
    }

    @Test
    void listBeersWithPagination() {

        BeerSearchRequest request = BeerSearchRequest.builder()
                .pageNumber(0)
                .pageSize(2)
                .build();

        Page<BeerDTO> beers = beerClient.listBeers(request);

        assertThat(beers.getNumber(), is(0));
        assertThat(beers.getSize(), is(2));
        assertThat(beers.getContent(), hasSize(2));
    }


    @Test
    void testGetBeerById() {
        Page<BeerDTO> beerDTOS = beerClient.listBeers(BeerSearchRequest.builder().build());
        BeerDTO beerDTO = beerDTOS.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(beerDTO.getId());

        assertThat(byId, notNullValue());
        assertThat(beerDTO.getBeerName(), notNullValue());

    }


    @Test
    void testCreateBeer() {
        BeerDTO newDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle("ALE")
                .upc("123456789")
                .quantityOnHand(100)
                .price(new BigDecimal("12.99"))
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDTO);

        assertThat(beerDTO.getBeerName(), equalTo("Test Beer"));

    }


    @Test
    void updateBeer() {
        // Create a new beer
        BeerDTO newDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle("ALE")
                .upc("123456789")
                .quantityOnHand(100)
                .price(new BigDecimal("10.99"))
                .build();

        BeerDTO createdBeer = beerClient.createBeer(newDTO);

        // Modify the beer
        createdBeer.setBeerName("Updated Test Beer");
        createdBeer.setPrice(new BigDecimal("15.99"));

        // Update the beer
        BeerDTO updatedBeer = beerClient.updateBeer(createdBeer);

        // Assert
        assertThat(updatedBeer.getBeerName(), is("Updated Test Beer"));
        assertThat(updatedBeer.getPrice(), is(new BigDecimal("15.99")));
        assertThat(updatedBeer.getUpc(), is("123456789"));
    }


    @Test
    void deleteBeer() {
        // Create a new beer
        BeerDTO newDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle("ALE")
                .upc("123456789")
                .quantityOnHand(100)
                .price(new BigDecimal("10.99"))
                .build();

        BeerDTO createdBeer = beerClient.createBeer(newDTO);

        // Modify the beer
        createdBeer.setBeerName("Updated Test Beer");
        createdBeer.setPrice(new BigDecimal("15.99"));

        // Update the beer
        beerClient.deleteBeer(createdBeer.getId());

        // Assert
        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(newDTO.getId());
        });
    }
}
