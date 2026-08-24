package guru.springframework.spring7resttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPageImpl extends PageImpl<BeerDTO> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(
            @JsonProperty("content") List<BeerDTO> content,
            @JsonProperty("_embedded") Embedded embedded,
            @JsonProperty("number") Integer number,
            @JsonProperty("size") Integer size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("page") PageMetadata page) {

        super(
                resolveContent(content, embedded),
                PageRequest.of(resolvePageNumber(number, page), resolvePageSize(size, page, content, embedded)),
                resolveTotalElements(totalElements, page, content, embedded)
        );
    }

    private static List<BeerDTO> resolveContent(List<BeerDTO> content, Embedded embedded) {
        if (content != null) {
            return content;
        }

        if (embedded != null && embedded.beer() != null) {
            return embedded.beer();
        }

        return List.of();
    }

    private static int resolvePageNumber(Integer number, PageMetadata page) {
        if (number != null) {
            return number;
        }

        if (page != null && page.number() != null) {
            return page.number();
        }

        return 0;
    }

    private static int resolvePageSize(
            Integer size,
            PageMetadata page,
            List<BeerDTO> content,
            Embedded embedded) {

        Integer resolvedSize = size != null ? size : page != null ? page.size() : null;

        return resolvedSize != null && resolvedSize > 0
                ? resolvedSize
                : Math.max(resolveContent(content, embedded).size(), 1);
    }

    private static long resolveTotalElements(
            Long totalElements,
            PageMetadata page,
            List<BeerDTO> content,
            Embedded embedded) {

        if (totalElements != null) {
            return totalElements;
        }

        if (page != null && page.totalElements() != null) {
            return page.totalElements();
        }

        return resolveContent(content, embedded).size();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Embedded(@JsonProperty("beer") List<BeerDTO> beer) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PageMetadata(
            @JsonProperty("number") Integer number,
            @JsonProperty("size") Integer size,
            @JsonProperty("totalElements") Long totalElements) {
    }
}
