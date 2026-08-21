package guru.springframework.spring7resttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPageImpl<BeerDTO> extends PageImpl<guru.springframework.spring7resttemplate.model.BeerDTO> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(
            @JsonProperty("_embedded") Embedded embedded,
            @JsonProperty("page") PagedModel.PageMetadata page) {

        super(
                embedded.beer(),
                PageRequest.of((int) page.number(), (int) page.size()),
                page.totalElements()
        );
    }
}