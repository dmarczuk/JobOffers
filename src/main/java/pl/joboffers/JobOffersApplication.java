package pl.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;
import pl.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OfferFetcherRestTemplateConfigurationProperties.class, JwtConfigurationProperties.class})
@EnableMongoRepositories
public class JobOffersApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }

}
