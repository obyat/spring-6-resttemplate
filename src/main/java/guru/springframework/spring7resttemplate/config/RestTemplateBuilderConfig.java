package guru.springframework.spring7resttemplate.config;

import jakarta.persistence.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.autoconfigure.RestTemplateBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;


@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.rootUrl}")
    String rootUrl;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer){
        RestTemplateBuilder restTemplateBuilder = restTemplateBuilderConfigurer.configure(new RestTemplateBuilder());
        DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory(rootUrl);

        return restTemplateBuilder.uriTemplateHandler(builderFactory);
    }
}
